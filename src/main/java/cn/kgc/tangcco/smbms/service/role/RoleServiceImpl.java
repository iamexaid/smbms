package cn.kgc.tangcco.smbms.service.role;

import java.sql.Connection;
import java.util.List;
import javax.annotation.Resource;

import cn.kgc.tangcco.smbms.dao.BaseDao;
import cn.kgc.tangcco.smbms.dao.role.RoleDao;
import cn.kgc.tangcco.smbms.pojo.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleDao roleDao;
	
	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Role> roleList = null;
		try {
			connection = BaseDao.getConnection();
			roleList = roleDao.getRoleList(connection);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return roleList;
	}
	
}
