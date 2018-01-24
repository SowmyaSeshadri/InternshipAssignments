package com.ztech.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.ztech.Constants.Constants;
import com.ztech.beans.CompanyDetails;
import com.ztech.beans.StudentDetails;
import com.ztech.dbutils.DBUtils;

public class OthersDAOImpl implements OthersDAO {

	private static final Logger logger = Logger.getLogger(OthersDAOImpl.class.getName());
	private static Connection conn = null;
	private static PreparedStatement pst = null;
	private static ResultSet rs = null;

	public int checkEligibilty(int regno, int companyid) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = conn.prepareStatement(Constants.CHECK_ELIGIBILITY);
			pst.setInt(1, regno);
			pst.setInt(2, companyid);
			rs = pst.executeQuery();
			if (!rs.next()) {
				logger.warning("The company ID or registration number is wrong.");
				return 0;
			}
			if (rs.getFloat(1) < rs.getFloat(3) || rs.getInt(2) > rs.getInt(4)) {
				return 1;
			} else {
				return 2;
			}
		} catch (SQLException e) {
			logger.warning("Error connecting with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return -1;
	}

	public ArrayList<StudentDetails> displayDetailsDepartment(String deptName) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			ArrayList<StudentDetails> studentArrayList = new ArrayList<StudentDetails>();
			StudentDetails studentDetails;
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.DISPLAY_DEPARTMENT_ALL);
			pst.setString(1, deptName);
			rs = pst.executeQuery();
			while (rs.next()) {
				studentDetails = new StudentDetails();
				studentDetails.setRegno(rs.getInt(1));
				studentDetails.setName(rs.getString(2));
				studentDetails.setArrears(rs.getInt(5));
				studentDetails.setCgpa(rs.getFloat(4));
				studentDetails.setEmail(rs.getString(3));
				studentDetails.setPlacedStatus(rs.getString(6));
				studentDetails.setDeptName(rs.getString(7));
				studentArrayList.add(studentDetails);
			}
			return studentArrayList;
		} catch (SQLException e) {
			logger.warning("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return null;
	}

	public int noOfStudentsPlaced(String deptName) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			if (deptName.equals("")) {
				pst = (PreparedStatement) conn.prepareStatement(Constants.TOTAL_STUDENTS_PLACED);
				pst.setString(1, "yes");
				rs = pst.executeQuery();
				rs.next();
				return rs.getInt(1);
			} else {
				pst = (PreparedStatement) conn.prepareStatement(Constants.TOTAL_STUDENTS_PLACED_DEPARTMENT);
				pst.setString(1, deptName);
				pst.setString(2, "yes");
				rs = pst.executeQuery();
				rs.next();
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.info("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return -1;
	}

	public int placementPercentage(String deptName) throws SQLException {
		int result, totalCount, placedCount;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			if (deptName.equals("")) {
				pst = (PreparedStatement) conn.prepareStatement(Constants.TOTAL_STUDENTS);
				rs = pst.executeQuery();
				rs.next();
				totalCount = rs.getInt(1);
				pst = (PreparedStatement) conn.prepareStatement(Constants.TOTAL_STUDENTS_PLACED);
				pst.setString(1, "yes");
				rs = pst.executeQuery();
				rs.next();
				placedCount = rs.getInt(1);
				result = (placedCount * 100 / totalCount);
			} else {
				pst = (PreparedStatement) conn.prepareStatement(Constants.TOTAL_STUDENTS_DEPARTMENT);
				pst.setString(1, deptName);
				rs = pst.executeQuery();
				rs.next();
				totalCount = rs.getInt(1);
				pst = (PreparedStatement) conn.prepareStatement(Constants.TOTAL_STUDENTS_PLACED_DEPARTMENT);
				pst.setString(1, deptName);
				pst.setString(2, "yes");
				rs = pst.executeQuery();
				rs.next();
				placedCount = rs.getInt(1);
				result = (placedCount * 100 / totalCount);
			}
			return result;
		} catch (SQLException e) {
			logger.warning("Error retrieving values from MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return -1;
	}

	public String validateTeacher(String department, String password) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.VALIDATE_TEACHER);
			pst.setString(1, department);
			pst.setString(2, password);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			logger.warning("Error connecting it with MySQL");
			return "";
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return "";
	}

	public String enterStudentsPlaced(int companyid, int regno) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.CHECK_STUDENT);
			pst.setInt(1, regno);
			rs = pst.executeQuery();
			if (!rs.next()) {
				logger.warning("The student registration number is wrong.");
				return "The student registration number is wrong.";
			}
			pst = (PreparedStatement) conn.prepareStatement(Constants.CHECK_COMPANY);
			pst.setInt(1, companyid);
			rs = pst.executeQuery();
			if (!rs.next()) {
				logger.warning("The company ID entered is wrong.");
				return "The company ID entered is wrong.";
			}
			pst = (PreparedStatement) conn.prepareStatement(Constants.VERIFY_PLACED_OR_NOT);
			pst.setInt(1, regno);
			rs = pst.executeQuery();
			if (rs.next()) {
				logger.warning("The student is already placed.");
				return "The student is already placed.";
			}
			pst = (PreparedStatement) conn.prepareStatement(Constants.INSERT_STUDENTS_PLACED);
			pst.setInt(1, companyid);
			pst.setInt(2, regno);
			pst.executeUpdate();
			pst = (PreparedStatement) conn.prepareStatement(Constants.UPDATE_PLACED_STATUS);
			pst.setString(1, "yes");
			pst.setInt(2, regno);
			pst.executeUpdate();
			return "The new entry is successfully done";
		} catch (SQLException e) {
			logger.warning("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return "";
	}

	public ArrayList<CompanyDetails> getCompanyList() throws SQLException {
		ArrayList<CompanyDetails> companyArrayList = new ArrayList<CompanyDetails>();
		CompanyDetails companyDetails = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.GET_COMPANY_LIST);
			rs = pst.executeQuery();
			while (rs.next()) {
				companyDetails = new CompanyDetails();
				companyDetails.setCompanyid(rs.getInt(1));
				companyDetails.setName(rs.getString(2));
				companyArrayList.add(companyDetails);
			}
			return companyArrayList;
		} catch (SQLException e) {
			logger.warning("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return null;
	}
	
	public ArrayList<String> getDepartmentList() throws SQLException {
		ArrayList<String> departmentList = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.GET_DEPARTMENT_LIST);
			rs = pst.executeQuery();
			while (rs.next()) {
				departmentList.add(rs.getString(1));
			}
			return departmentList;
		} catch (SQLException e) {
			logger.warning("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return null;
	}
	
	public ArrayList<Integer> getTotalStudentsList() throws SQLException {
		ArrayList<Integer> studentsCountList = new ArrayList<Integer>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.GET_STUDENTS_COUNT_LIST);
			rs = pst.executeQuery();
			while (rs.next()) {
				studentsCountList.add(rs.getInt(1));
			}
			return studentsCountList;
		} catch (SQLException e) {
			logger.warning("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return null;
	}

	public String getCompanyName(int companyid) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DBUtils.getConnection();
			pst = (PreparedStatement) conn.prepareStatement(Constants.GET_COMPANY_NAME);
			pst.setInt(1, companyid);
			rs = pst.executeQuery();
			rs.next();
			String companyName = rs.getString(1);
			return companyName;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warning("Error connecting it with MySQL");
		} catch (ClassNotFoundException e) {
			logger.warning("Class not found for SQL Driver.");
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
		}
		return null;
	}
	
}