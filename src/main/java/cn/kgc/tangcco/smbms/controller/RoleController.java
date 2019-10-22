package cn.kgc.tangcco.smbms.controller;

import cn.kgc.tangcco.smbms.pojo.Role;
import cn.kgc.tangcco.smbms.service.role.RoleService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @RequestMapping(value = "/rolelist",method = RequestMethod.POST)
    @ResponseBody
    public JSON showRoleList(){
        List<Role> roleList=roleService.getRoleList();
        return (JSON) JSON.toJSON(roleList);
    }
}
