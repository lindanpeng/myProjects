package com.impl;



import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.entity.Friend;
@Component("friendDaoImpl")
public class FriendDaoImpl extends BaseDaoImpl<Friend> {
	public void delete(String user, String friend) {
       Session session=sessionFactory.getCurrentSession();
       Query query=session.createQuery("delete from Friend where userid =:userid and friendid =:friendid");
       query.setParameter("userid",user);
       query.setParameter("friendid",friend);
       query.executeUpdate();
	}
	public List<String> getList(String userid){
		Session session=sessionFactory.getCurrentSession();
		List<String> friends=session.createQuery("select friendid from Friend where userid ='"+userid+"'").list();
		return friends;
	}
	public boolean isFriends(String userid, String friendid) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Friend where userid =:userid and friendid=:friendid");
		query.setParameter("userid",userid);
		query.setParameter("friendid", friendid);
		return query.uniqueResult()==null?false:true;
	}
}
