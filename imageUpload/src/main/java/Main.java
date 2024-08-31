import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.MovieDAO;
import dto.MovieDTO;

public class Main {

	private static final String DB_URL = "jdbc:postgresql://localhost:5432/jdbc";
	private static final String DB_USER = "postgres";
	private static final String DB_PASS = "password";

	public static void main(String[] args) throws SQLException {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		MovieDAO dao = new MovieDAO(conn);

		/*
		テキスト通り
		ArrayList<MovieDTO> list = dao.selectAll(3);
		*/

		/*
		 * 数字を入力する場合
		Scanner scanner = new Scanner(System.in);
		System.out.println("何番目の映画まで見ますか？　数値を入力してください:");
		int number = scanner.nextInt();
		System.out.println("id = " + number + " までの映画を出力します。");
		
		ArrayList<MovieDTO> list = dao.selectAll(number);
		 */

		/*
		 * 数字を入力し、idと比較して最大値までを任意条件式の元で出力する場合
		 */
		Scanner scanner = new Scanner(System.in);
		System.out.println("何番目の映画まで見ますか？　数値を入力してください:");

		int number = scanner.nextInt();

		ArrayList<MovieDTO> list = dao.selectEvenNum(number);
		ArrayList<MovieDTO> listMax = dao.selectMaxId(number);
		
		scanner.close();

		for (MovieDTO dtoMax : listMax) {
			//			System.out.println("   Id_Max : " + dtoMax.getId());
			//			↑確認のための出力
			int maxId = dtoMax.getId();
			int maxNumber;

			if (number > maxId) {
				maxNumber = maxId;
			} else {
				maxNumber = number;
			}
			System.out.print("idの最大値は " + maxNumber + "です。　　");
			System.out.println("id = " + maxNumber + " までの偶数番目の映画を出力します。");
			System.out.println();
		}

		// 結果表示のfor文
		int i = 0;

		for (MovieDTO dto : list) {
			i++;
			System.out.print(i + "番目の映画 ◆◆◆ ");
			System.out.println("Id : " + dto.getId());
			System.out.println("    │  Title    : " + dto.getTitle());
			System.out.println("    │  Director : " + dto.getDirector());
			System.out.println("    │  Duration : " + dto.getDuration());
			System.out.println("    └──────-────-────-");
		}
		if (i < number) {
			System.out.println("合計 " + i + " 本の映画を出力しました。");
		} else {
			System.out.println("合計 " + number + " 本の映画を出力しました。");
		}
	}

}
