package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.UserInfo;

@Service @Transactional
public class UserInfoDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*����ͼ����Ϣ*/
    public void AddUserInfo(UserInfo userInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(userInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<UserInfo> QueryUserInfoInfo(String user_name,String realName,String birthday,String cardNumber,String city,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From UserInfo userInfo where 1=1";
    	if(!user_name.equals("")) hql = hql + " and userInfo.user_name like '%" + user_name + "%'";
    	if(!realName.equals("")) hql = hql + " and userInfo.realName like '%" + realName + "%'";
    	if(!birthday.equals("")) hql = hql + " and userInfo.birthday like '%" + birthday + "%'";
    	if(!cardNumber.equals("")) hql = hql + " and userInfo.cardNumber like '%" + cardNumber + "%'";
    	if(!city.equals("")) hql = hql + " and userInfo.city like '%" + city + "%'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List userInfoList = q.list();
    	return (ArrayList<UserInfo>) userInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<UserInfo> QueryUserInfoInfo(String user_name,String realName,String birthday,String cardNumber,String city) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From UserInfo userInfo where 1=1";
    	if(!user_name.equals("")) hql = hql + " and userInfo.user_name like '%" + user_name + "%'";
    	if(!realName.equals("")) hql = hql + " and userInfo.realName like '%" + realName + "%'";
    	if(!birthday.equals("")) hql = hql + " and userInfo.birthday like '%" + birthday + "%'";
    	if(!cardNumber.equals("")) hql = hql + " and userInfo.cardNumber like '%" + cardNumber + "%'";
    	if(!city.equals("")) hql = hql + " and userInfo.city like '%" + city + "%'";
    	Query q = s.createQuery(hql);
    	List userInfoList = q.list();
    	return (ArrayList<UserInfo>) userInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<UserInfo> QueryAllUserInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From UserInfo";
        Query q = s.createQuery(hql);
        List userInfoList = q.list();
        return (ArrayList<UserInfo>) userInfoList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String user_name,String realName,String birthday,String cardNumber,String city) {
        Session s = factory.getCurrentSession();
        String hql = "From UserInfo userInfo where 1=1";
        if(!user_name.equals("")) hql = hql + " and userInfo.user_name like '%" + user_name + "%'";
        if(!realName.equals("")) hql = hql + " and userInfo.realName like '%" + realName + "%'";
        if(!birthday.equals("")) hql = hql + " and userInfo.birthday like '%" + birthday + "%'";
        if(!cardNumber.equals("")) hql = hql + " and userInfo.cardNumber like '%" + cardNumber + "%'";
        if(!city.equals("")) hql = hql + " and userInfo.city like '%" + city + "%'";
        Query q = s.createQuery(hql);
        List userInfoList = q.list();
        recordNumber = userInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public UserInfo GetUserInfoByUser_name(String user_name) {
        Session s = factory.getCurrentSession();
        UserInfo userInfo = (UserInfo)s.get(UserInfo.class, user_name);
        return userInfo;
    }

    /*����UserInfo��Ϣ*/
    public void UpdateUserInfo(UserInfo userInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(userInfo);
    }

    /*ɾ��UserInfo��Ϣ*/
    public void DeleteUserInfo (String user_name) throws Exception {
        Session s = factory.getCurrentSession();
        Object userInfo = s.load(UserInfo.class, user_name);
        s.delete(userInfo);
    }

}