package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.MovieDTO;

public class MovieDAO {
	private Connection conn; // データベース接続オブジェクト

	// コンストラクタ
	public MovieDAO(Connection conn) {
		this.conn = conn;
	}

	// データ取得メソッド
	public ArrayList<MovieDTO> selectAll(int id) throws SQLException {
		ArrayList<MovieDTO> list = new ArrayList<>();
		String sql = "SELECT id, title, director, duration FROM movies WHERE id <= ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MovieDTO dto = new MovieDTO();
				dto.setId(rs.getInt("id"));
				dto.setTitle(rs.getString("title"));
				dto.setDirector(rs.getString("director"));
				dto.setDuration(rs.getInt("duration"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return list;
	}

	// 偶数列 降順 出力
	public ArrayList<MovieDTO> selectEvenNum(int id) throws SQLException {
		ArrayList<MovieDTO> list = new ArrayList<>();
		//		String sql = "SELECT * FROM movies WHERE id % 2 = 0";
		// ↑エラー
		//		String sql = "SELECT * FROM movies WHERE id <= ? ORDER BY id DESC";
		// ↑出力できた。降順だった
		//		String sql = "SELECT * FROM movies WHERE id % 2 = 0 and id <= ?";
		// ↑出力できた。昇順なので降順にしたい
		String sql = "SELECT * FROM movies WHERE id % 2 = 0 and id <= ? ORDER BY id DESC";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MovieDTO dto = new MovieDTO();
				dto.setId(rs.getInt("id"));
				dto.setTitle(rs.getString("title"));
				dto.setDirector(rs.getString("director"));
				dto.setDuration(rs.getInt("duration"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//			if (conn != null) {
			//				conn.close();
			//			}
		}
		return list;
	}

	// id 最大値 出力
	public ArrayList<MovieDTO> selectMaxId(int id) throws SQLException {
		ArrayList<MovieDTO> listMax = new ArrayList<>();
		//		String sqlMax = "SELECT max(id) FROM movies WHERE id <= ?";
		String sqlMax = "SELECT * FROM movies WHERE id <= ? ORDER BY id DESC FETCH NEXT 1 ROWS ONLY";
		try (PreparedStatement psMax = conn.prepareStatement(sqlMax)) {
			psMax.setInt(1, id);
			ResultSet rsMax = psMax.executeQuery();

			while (rsMax.next()) {
				MovieDTO dtoMax = new MovieDTO();
				dtoMax.setId(rsMax.getInt("id"));
				dtoMax.setTitle(rsMax.getString("title"));
				dtoMax.setDirector(rsMax.getString("director"));
				dtoMax.setDuration(rsMax.getInt("duration"));
				listMax.add(dtoMax);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return listMax;
	}

}
