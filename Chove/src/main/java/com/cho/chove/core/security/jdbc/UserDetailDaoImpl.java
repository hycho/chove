package com.cho.chove.core.security.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.cho.chove.core.security.entity.Authority;
import com.cho.chove.core.security.entity.User;

public class UserDetailDaoImpl extends JdbcDaoImpl{
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailDaoImpl.class);
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetails> users = loadUsersByUsername(username);
		
		if (users.size() == 0) {
			logger.debug("Query returned no results for user '" + username + "'");
			UsernameNotFoundException ue = new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));
			throw ue;
		}
		
		User user = (User)users.get(0); // contains no GrantedAuthority[]
		
		List<Authority> dbAuthsSet = new ArrayList<Authority>();
		
		if (getEnableAuthorities()) {
			dbAuthsSet.addAll((Collection<? extends Authority>) loadUserAuthorities(user.getUserId()));
		}
		
		if (getEnableGroups()) {
			dbAuthsSet.addAll((Collection<? extends Authority>) loadGroupAuthorities(user.getUserId()));
		}
		
		List<Authority> dbAuths = new ArrayList<Authority>(dbAuthsSet);
		user.setAuthorities(dbAuths);
		
		if (dbAuths.size() == 0) {
			logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");
			UsernameNotFoundException ue = new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.noAuthority", new Object[] {username}, "User {0} has no GrantedAuthority"));
			throw ue;
		}
		
		return user;
	}
	
	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[] {username}, new RowMapper<UserDetails>() {
			public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				String userId = rs.getString(1);
				String id = rs.getString(2);
				String password = rs.getString(3);
				String name = rs.getString(4);
				return new User(userId, id, password, name);
			}
		});
	}
	
	@Override
	protected List<GrantedAuthority> loadUserAuthorities(String username) {
		return getJdbcTemplate().query(getAuthoritiesByUsernameQuery(), new String[] {username}, new RowMapper<GrantedAuthority>() {
			public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
				String roleName = getRolePrefix() + rs.getString(1);
				return new Authority(roleName);
			}
		});
	}
}
