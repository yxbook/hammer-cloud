package com.fmkj.race.server.rabbitmq;
 
import java.util.HashMap;
import javax.annotation.Resource;
import com.fmkj.race.dao.domain.GcJoinactivityrecord;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author 杨胜彬
 * @comments 生产者类
 * @time 2018年9月5日
 * @developers 费马科技
 */
@Component
public class MessageProducer {

	@Autowired
	private AmqpTemplate amqpTemplate;

	public HashMap<String, Object> send(Object message) {
		
		HashMap<String, Object> map =  new HashMap<String, Object>();
		GcJoinactivityrecord joins = (GcJoinactivityrecord) message;
		try {
			//加入队列
			this.amqpTemplate.convertAndSend("workqueue", joins);
		} catch (AmqpException e) {
			map.put("message", "请求消息已满,请稍后提交!");
			return map;
		}
		map.put("code", "500");
		map.put("message", "参加活动成功");
		return map;
	}
 
}