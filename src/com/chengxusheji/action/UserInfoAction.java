package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class UserInfoAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�photo��������*/
	private File photoFile;
	private String photoFileFileName;
	private String photoFileContentType;
	public File getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(File photoFile) {
		this.photoFile = photoFile;
	}
	public String getPhotoFileFileName() {
		return photoFileFileName;
	}
	public void setPhotoFileFileName(String photoFileFileName) {
		this.photoFileFileName = photoFileFileName;
	}
	public String getPhotoFileContentType() {
		return photoFileContentType;
	}
	public void setPhotoFileContentType(String photoFileContentType) {
		this.photoFileContentType = photoFileContentType;
	}
    /*�������Ҫ��ѯ������: �û���*/
    private String user_name;
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return this.user_name;
    }

    /*�������Ҫ��ѯ������: ����*/
    private String realName;
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getRealName() {
        return this.realName;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String birthday;
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirthday() {
        return this.birthday;
    }

    /*�������Ҫ��ѯ������: ����֤*/
    private String cardNumber;
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getCardNumber() {
        return this.cardNumber;
    }

    /*�������Ҫ��ѯ������: ����*/
    private String city;
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return this.city;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource UserInfoDAO userInfoDAO;

    /*��������UserInfo����*/
    private UserInfo userInfo;
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    /*��ת������UserInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*����UserInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddUserInfo() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤�û����Ƿ��Ѿ�����*/
        String user_name = userInfo.getUser_name();
        UserInfo db_userInfo = userInfoDAO.GetUserInfoByUser_name(user_name);
        if(null != db_userInfo) {
            ctx.put("error",  java.net.URLEncoder.encode("���û����Ѿ�����!"));
            return "error";
        }
        try {
            /*������Ƭ�ϴ�*/
            String photoPath = "upload/noimage.jpg"; 
       	 	if(photoFile != null)
       	 		photoPath = photoUpload(photoFile,photoFileContentType);
       	 	userInfo.setPhoto(photoPath);
            userInfoDAO.AddUserInfo(userInfo);
            ctx.put("message",  java.net.URLEncoder.encode("UserInfo���ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("UserInfo����ʧ��!"));
            return "error";
        }
    }

    /*��ѯUserInfo��Ϣ*/
    public String QueryUserInfo() {
        if(currentPage == 0) currentPage = 1;
        if(user_name == null) user_name = "";
        if(realName == null) realName = "";
        if(birthday == null) birthday = "";
        if(cardNumber == null) cardNumber = "";
        if(city == null) city = "";
        List<UserInfo> userInfoList = userInfoDAO.QueryUserInfoInfo(user_name, realName, birthday, cardNumber, city, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        userInfoDAO.CalculateTotalPageAndRecordNumber(user_name, realName, birthday, cardNumber, city);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = userInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = userInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("userInfoList",  userInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("user_name", user_name);
        ctx.put("realName", realName);
        ctx.put("birthday", birthday);
        ctx.put("cardNumber", cardNumber);
        ctx.put("city", city);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryUserInfoOutputToExcel() { 
        if(user_name == null) user_name = "";
        if(realName == null) realName = "";
        if(birthday == null) birthday = "";
        if(cardNumber == null) cardNumber = "";
        if(city == null) city = "";
        List<UserInfo> userInfoList = userInfoDAO.QueryUserInfoInfo(user_name,realName,birthday,cardNumber,city);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "UserInfo��Ϣ��¼"; 
        String[] headers = { "�û���","����","����","�Ա�","��������","����֤","����","��Ƭ","��ͥ��ַ"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<userInfoList.size();i++) {
        	UserInfo userInfo = userInfoList.get(i); 
        	dataset.add(new String[]{userInfo.getUser_name(),userInfo.getPassword(),userInfo.getRealName(),userInfo.getSex(),new SimpleDateFormat("yyyy-MM-dd").format(userInfo.getBirthday()),userInfo.getCardNumber(),userInfo.getCity(),userInfo.getPhoto(),userInfo.getAddress()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"UserInfo.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯUserInfo��Ϣ*/
    public String FrontQueryUserInfo() {
        if(currentPage == 0) currentPage = 1;
        if(user_name == null) user_name = "";
        if(realName == null) realName = "";
        if(birthday == null) birthday = "";
        if(cardNumber == null) cardNumber = "";
        if(city == null) city = "";
        List<UserInfo> userInfoList = userInfoDAO.QueryUserInfoInfo(user_name, realName, birthday, cardNumber, city, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        userInfoDAO.CalculateTotalPageAndRecordNumber(user_name, realName, birthday, cardNumber, city);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = userInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = userInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("userInfoList",  userInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("user_name", user_name);
        ctx.put("realName", realName);
        ctx.put("birthday", birthday);
        ctx.put("cardNumber", cardNumber);
        ctx.put("city", city);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�UserInfo��Ϣ*/
    public String ModifyUserInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������user_name��ȡUserInfo����*/
        UserInfo userInfo = userInfoDAO.GetUserInfoByUser_name(user_name);

        ctx.put("userInfo",  userInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�UserInfo��Ϣ*/
    public String FrontShowUserInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������user_name��ȡUserInfo����*/
        UserInfo userInfo = userInfoDAO.GetUserInfoByUser_name(user_name);

        ctx.put("userInfo",  userInfo);
        return "front_show_view";
    }

    /*�����޸�UserInfo��Ϣ*/
    public String ModifyUserInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*������Ƭ�ϴ�*/
            if(photoFile != null) {
            	String photoPath = photoUpload(photoFile,photoFileContentType);
            	userInfo.setPhoto(photoPath);
            }
            userInfoDAO.UpdateUserInfo(userInfo);
            ctx.put("message",  java.net.URLEncoder.encode("UserInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("UserInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��UserInfo��Ϣ*/
    public String DeleteUserInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            userInfoDAO.DeleteUserInfo(user_name);
            ctx.put("message",  java.net.URLEncoder.encode("UserInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("UserInfoɾ��ʧ��!"));
            return "error";
        }
    }

}