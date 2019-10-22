package cn.kgc.tangcco.smbms.controller;

import cn.kgc.tangcco.smbms.pojo.Role;
import cn.kgc.tangcco.smbms.pojo.User;
import cn.kgc.tangcco.smbms.service.role.RoleService;
import cn.kgc.tangcco.smbms.service.user.UserService;
import cn.kgc.tangcco.smbms.tools.Constants;
import cn.kgc.tangcco.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger=Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    /**
     * 查询用户信息
     * @param model
     * @param queryUserName
     * @param queryUserRole
     * @param pageIndex
     * @return
     */
    @RequestMapping(value = "userList.html")
    public String showUserList(Model model, @RequestParam(value = "queryname",required = false)String queryUserName,
                               @RequestParam(value = "queryUserRole",required = false)Integer queryUserRole,
                               @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.info("用户名"+queryUserName);
        logger.info("角色编号"+queryUserRole);
        logger.info("当前页码"+pageIndex);
        int _queryUserRole = 0;
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        if(queryUserName == null){
            queryUserName = "";
        }
        if(queryUserRole != null && !queryUserRole.equals("")){
            _queryUserRole =queryUserRole;
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                return "redirect:/user/syserror.html";
                //response.sendRedirect("syserror.jsp");
            }
        }
        //总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,_queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        userList = userService.getUserList(queryUserName,_queryUserRole,currentPageNo,pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    /**
     * 去添加用户页面
     * @param user
     * @return
     */
    @RequestMapping(value = "touseradd.html",method = RequestMethod.GET)
    public String toUseradd(@ModelAttribute("user") User user){
        return "useradd";
    }

    /**
     * 判断用户编码是否存在
     * @param userCode
     * @return
     */
    @RequestMapping("ucExists")
    @ResponseBody
    public JSON ucExists(String userCode){
        User user=userService.selectUserCodeExist(userCode);
        Map<String,String> resultMap=new HashMap<>();
        if (user==null){
            resultMap.put("mess","noexists");
        }else{
            resultMap.put("mess","exists");
        }
        return (JSON) JSON.toJSON(resultMap);
    }
@RequestMapping(value = "saveuser.html",method = RequestMethod.POST)
public String saveUser(User user, HttpSession session){

    user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
    user.setCreationDate(new Date());
    //添加用户
    boolean result=userService.add(user);
    if (result){//添加成功
        return "redirect:/user/userlist.html";
    }else{
        return "useradd";
    }

}

}
