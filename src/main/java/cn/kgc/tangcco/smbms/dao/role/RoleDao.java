package cn.kgc.tangcco.smbms.dao.role;

import cn.kgc.tangcco.smbms.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {
	
	public List<Role> getRoleList(Connection connection)throws Exception;

}
