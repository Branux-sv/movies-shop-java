package org.branuxsv.rentalmovies.dao;

import org.branuxsv.rentalmovies.model.User;

public interface IUserDao extends IDaoGeneric<User> {

	User authenticate(String username, String PassWord);
	User existsUserByNameOrEmail(String username, String email);
	User existsUserByConfirmToken(String token);
}
