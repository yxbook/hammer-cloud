package com.fmkj.race.server.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.domain.GcActivity;
import com.fmkj.race.dao.domain.HcAccount;
import com.fmkj.race.dao.mapper.GcActivityMapper;
import com.fmkj.race.dao.mapper.GcJoinactivityrecordMapper;
import com.fmkj.race.dao.domain.GcJoinactivityrecord;
import com.fmkj.race.dao.mapper.HcAccountMapper;
import com.fmkj.race.server.hammer.contracts.PuzzleHammer.puzzle.Helper;
import com.fmkj.race.server.hammer.contracts.PuzzleHammer.puzzle.Person;
import com.fmkj.race.server.hammer.contracts.PuzzleHammer.puzzle.State;
import com.fmkj.race.server.service.GcJoinactivityrecordService;
import com.fmkj.race.server.util.CalendarTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
* @Description: GcJoinactivityrecord Service实现
* @Author: yangshengbin
* @CreateDate: 2018/9/5.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class GcJoinactivityrecordServiceImpl extends BaseServiceImpl<GcJoinactivityrecordMapper, GcJoinactivityrecord> implements GcJoinactivityrecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcJoinactivityrecordServiceImpl.class);

    @Autowired
    private GcJoinactivityrecordMapper gcJoinactivityrecordMapper;

    @Autowired
    private HcAccountMapper hcAccountMapper;

    @Autowired
    private GcActivityMapper gcActivityMapper;



    /**
     * @author yangshengbin
     * @Description：插入用户参与记录/更改用户p能量值/
     * @date 2018/9/6 0006 10:02
     * @param aid
     * @param joins
     * @param par
     * @return boolean
    */
    public boolean addGcJoinactivityrecordAndUpAccount(Integer aid, GcJoinactivityrecord joins, double par) {

        //插入参与记录
        CalendarTime clt = new CalendarTime();
        Timestamp btime = clt.thisDate();//获取当前时间
        joins.setTime(btime);
        joins.setIsChain(0);
        Integer result = 0;
        try {
            result = gcJoinactivityrecordMapper.insert(joins);
        } catch (Exception e) {
            throw new RuntimeException("插入参与记录异常,"+"活动aid:"+aid+",用户:"+joins.getUid()+"" + e.getMessage());
        }

        //更改用户p能量
        if (result>0){
            HcAccount hcAccount = new HcAccount();
            hcAccount.setId(joins.getUid());
            HcAccount hcAccount1 = hcAccountMapper.selectOne(hcAccount);//获取用户原有p能量
            if (Double.doubleToLongBits(hcAccount1.getMyP()) < Double.doubleToLongBits(par)){
                System.err.println("你拥有的p能量不够");
                return false;
            }
            double newMyp = hcAccount1.getMyP() - par;//用户新的p能量
            hcAccount.setMyP(newMyp);
            Integer res = 0;
            try {
                res = hcAccountMapper.updateById(hcAccount);
            } catch (Exception e) {
                throw new RuntimeException("7用户参加活动更新p能量异常,"+"活动aid:"+aid+",用户:"+joins.getUid()+"" + e.getMessage());
            }
            if (res<=0){
                System.err.println("用户参加活动更新p能量失败");
                return false;
            }
            return true;
        }
        return false;
    }






    /**
     * @author yangshengbin
     * @Description：获取用户合约地址
     * @date 2018/9/6 0006 10:58
     * @param aid
     * @return java.lang.String
    */
    public String queryGcActivityByContract(Integer aid) {
        GcActivity ga = new GcActivity();
        ga.setId(aid);
        GcActivity activity = null;
        String contract = null;
        try {
            activity = gcActivityMapper.selectOne(ga);
        } catch (Exception e) {
            throw new RuntimeException("获取合约地址异常" + e.getMessage());
        }
        if (activity!=null) {
            contract = activity.getContract();
        }
        return contract;
    }






    /**
     * @author yangshengbin
     * @Description：参加活动加载合约
     * @date 2018/9/6 0006 11:03
     * @param contract
     * @param aid
     * @param uid
     * @return boolean
    */
    public boolean participateActivity(String contract, Integer aid, Integer uid) {
        if (contract != null) {//  如果合约地址不为空。根据合约地址把参加活动的人上到对应链上
            Helper helper = new Helper();
            boolean init = helper.init();// 合约实例初始化
            if (!init) {
                System.err.println("合约初始化失败");
                return false;
            }
            boolean loadContract = helper.loadContract(contract);//加载合约
            if (!loadContract) {
                System.err.println("合约地址加载失败");
                return false;
            }

            helper.changeStage(State.participate);
            HcAccount ha = new HcAccount();
            ha.setId(uid);
            HcAccount user = hcAccountMapper.selectOne(ha);// 获取参与活动的用户信息

            System.err.println("用户:"+uid + "参与活动:" + aid);

            helper.particiPuzzle(new Person(user.getNickname(), BigInteger.valueOf(uid)));// 实例出合约用户，参与活动
            System.err.println("参加活动结束");


            //将用户上链记录改为1
            GcJoinactivityrecord gcJoinactivityrecord = new GcJoinactivityrecord();
            gcJoinactivityrecord.setIsChain(1);
            GcJoinactivityrecord gcJoinactivityrecord1 = new GcJoinactivityrecord();
            gcJoinactivityrecord1.setUid(uid);
            EntityWrapper<GcJoinactivityrecord> entityWrapper = new EntityWrapper<GcJoinactivityrecord>();
            entityWrapper.setEntity(gcJoinactivityrecord1);

            try {
                Integer res = gcJoinactivityrecordMapper.update(gcJoinactivityrecord,entityWrapper);
            } catch (Exception e) {
                throw new RuntimeException("更改用户上链记录为1异常" + e.getMessage());
            }
            helper.release();
        }
        return true;
    }





    @Override
    /**
     * @author yangshengbin
     * @Description：最后一个用户参与活动
     * @date 2018/9/6 0006 14:12
     * @param contract
     * @param aid
     * @return boolean
    */
    public boolean initAndloadContractAndChangeStage(String contract, Integer aid) {
        Helper helper = new Helper();
        // 合约实例初始化
        boolean init = helper.init();
        if (!init) {
            System.err.println("合约初始化失败");
            return false;
        }

        //加载合约
        boolean loadContract = helper.loadContract(contract);
        if (!loadContract) {
            System.err.println("合约地址加载失败");
            return false;
        }

        //更新活动状态
        int res = updateGcJoinacTivityByStatus(aid);
        if (res<=0) {
            System.err.println("更新活动状态失败");
            return false;
        }

        //通知活动关闭
        boolean stage = helper.changeStage(State.closed);
        if(!stage) {
            System.err.println("合约关闭失败");
            return false;
        }
        return true;
    }



    /**
     * @author 杨胜彬
     * @comments 更新活动状态
     * @time 2018年8月8日 上午11:22:10
     * @param aid
     * @return
     * @returnType int
     * @modification
     */
    public int updateGcJoinacTivityByStatus(Integer aid) {
        GcActivity gat = new GcActivity();
        gat.setId(aid);
        gat.setStatus(5);
        int res;
        try {
            res = gcActivityMapper.updateById(gat);
        } catch (Exception e) {
            System.err.println("更新活动状态异常");
            throw new RuntimeException("更新活动状态异常" + e.getMessage());
        }
        return res;
    }




}