package com.fmkj.common.comenum;

/**
 * @Description:
 * 签到       1飞羽/次
 * 参与活动	  2飞羽/次
 * 发布活动	 10飞羽/次
 * 购买CNT	 2飞羽/次
 * 邀请人注册	 5飞羽/次
 * 上传头像	 5飞羽
 * 绑定邮箱	 10飞羽
 * 自己注册	 10飞羽
 * 绑定支付宝	  15飞羽
 * 绑定微信支付	15飞羽
 * 实名认证	 20飞羽
 * 完成任务	 1飞羽/次
 * 充值虚拟货币	5飞羽/次
 * 任务中心发布任务	10飞羽/次
 * 兑换飞羽	 1飞羽/1CNT
 * @Author: youxun
 * @Version: 1.0
 **/
public enum PointEnum {
    //签到
    SIGN_IN(1,1D),
    //参与活动
    PART_ACITIVITY(2,2D),
    //发布活动
    PUBLISH_ACITIVITY(3,10D),
    //购买CNT
    BUY_CTN(4,2D),
    //邀请人注册
    INVIT_REGISTER(5,5D),
    //上传头像
    UPLOAD_HEAD(6,5D),
    //绑定邮箱
    BIND_EMAIL(7,10D),
    //自己注册
    SELF_REGISTER(8,10D),
    //绑定支付宝
    BIND_ALIPAY(9,15D),
    //绑定微信支付
    BIND_WEBCHAT_PAY(10,15D),
    //实名认证
    REAL_AUTHENT(11,20D),
    //完成任务
    FINISH_TASK(12,1D),
    //充值虚拟货币
    CHARGE_VIRT_CURRENCY(13,5D),
    //任务中心发布任务
    PUBLISH_TASK(14,10D),
    //兑换飞羽
    EXCHANGE_POINT(15,1D);


    public int pointId;
    public Double pointNum;

    private PointEnum(int pointId, double pointNum) {
        this.pointId = pointId;
        this.pointNum = pointNum;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public double getPointNum() {
        return pointNum;
    }

    public void setPointNum(double pointNum) {
        this.pointNum = pointNum;
    }
}
