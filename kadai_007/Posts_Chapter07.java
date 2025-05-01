package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {

    public static void main(String[] args) {
        Connection con = null;

         String[][] userList = {
            { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
            { "1002", "2023-02-08", "お疲れ様です！", "12" },
            { "1003", "2023-02-09", "今日も頑張ります！", "18" },
            { "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
            { "1002", "2023-02-10", "明日から連休ですね！", "20" }
        };

        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "Dh291422!"
            );

            System.out.println("データベース接続成功：" + con);

            System.out.println("レコード追加を実行します");

            String insertSql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES "
                    + "(?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?)";
            PreparedStatement insertPs = con.prepareStatement(insertSql);

            int index = 1;
            for (String[] row : userList) {
                insertPs.setInt(index++, Integer.parseInt(row[0]));
                insertPs.setDate(index++, Date.valueOf(row[1]));
                insertPs.setString(index++, row[2]);
                insertPs.setInt(index++, Integer.parseInt(row[3]));
            }

            int inserted = insertPs.executeUpdate();
            insertPs.close();

            System.out.println(inserted + "件のレコードが追加されました");

            System.out.println("ユーザーIDが1002のレコードを検索しました");

            String selectSql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = ?";
            PreparedStatement selectPs = con.prepareStatement(selectSql);
            selectPs.setInt(1, 1002);

            ResultSet result = selectPs.executeQuery();

            int count = 1;
            while (result.next()) {
                Date postedAt = result.getDate("posted_at");
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");

                System.out.println(count + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);

                count++;
            }

            result.close();
            selectPs.close();

        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
}
