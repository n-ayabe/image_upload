<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Display Images</title>
<style>
/* image_container のスタイル */
.image_container {
    display: flex; /* Flexboxを使用 */
    justify-content: space-between; /* 画像の間にスペースを均等に配置 */
    align-items: flex-start; /* 画像の上端を揃える */
}

/* origin_image のスタイル */
.origin_image {
    flex: 1; /* 横幅を均等に設定 */
    margin-right: 10px; /* origin_image と mosaic_image の間にマージンを設定 */
}

/* mosaic_image のスタイル */
.mosaic_image {
    flex: 1; /* 横幅を均等に設定 */
    margin-left: 10px; /* mosaic_image と origin_image の間にマージンを設定 */
}

/* h2 と p のスタイル (必要に応じて) */
.origin_image h2, .mosaic_image h2 {
    margin: 0;
}

.origin_image p, .mosaic_image p {
    margin: 0;
}
</style>
</head>

<body>
    <h1>Display Images</h1>
    <%
        String dbUrl = "jdbc:postgresql://localhost:5432/jdbc";
        String dbUser = "postgres";
        String dbPassword = "password";
        String uploadDir = "img/image_origin"; // Directory for uploaded images
        String mosaicDir = "img/image_mosaic"; // Directory for mosaic images

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String sql = "SELECT img FROM images";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
                 java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String imagePath = rs.getString("img");
                    String imageName = imagePath.substring(imagePath.lastIndexOf('/') + 1); // Extract file name

                    // Construct paths for both original and mosaic images
                    String fullImagePath = request.getContextPath() + "/" + uploadDir + "/" + imageName;
                    String fullMosaicPath = request.getContextPath() + "/" + mosaicDir + "/" + imageName;
    %>
					<div class="image_container">
						<div class="origin_image">
    	                	<h2>Original Image</h2>
        	        	    <img src="<%= fullImagePath %>" alt="Original Image" style="max-width: 300px; max-height: 300px;" />
            	        </div>
						<div class="mosaic_image">
        	    	        <h2>Mosaic Image</h2>
    	            	    <img src="<%= fullMosaicPath %>" alt="Mosaic Image" style="max-width: 300px; max-height: 300px;" />
	                    </div>
                    </div>
    <%
                }
            }
        } catch (java.sql.SQLException e) {
            out.println("Database error: " + e.getMessage());
        }
    %>
</body>
</html>
