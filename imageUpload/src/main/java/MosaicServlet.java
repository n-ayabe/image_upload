import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/MosaicServlet")
@MultipartConfig
public class MosaicServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydatabase"; // 正しいデータベース名に修正
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";
    private static final String UPLOAD_DIR = "img/image_origin"; // Original image directory
    private static final String MOSAIC_DIR = "img/image_mosaic"; // Mosaic image directory

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // JDBCドライバーの明示的なロード（JDBC 4.0以降は通常不要）
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        }

        Part filePart = request.getPart("image"); // Retrieves <input type="file" name="image">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Get the original file name

        // Generate a unique file name by appending timestamp
        String uniqueFileName = generateUniqueFileName(fileName);

        // Get the absolute path of the upload directory inside webapp
        String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
        String mosaicPath = getServletContext().getRealPath("/") + File.separator + MOSAIC_DIR;

        // Create directories if they do not exist
        createDirectoryIfNotExists(uploadPath);
        createDirectoryIfNotExists(mosaicPath);

        File originalFile = new File(uploadPath + File.separator + uniqueFileName);
        File mosaicFile = new File(mosaicPath + File.separator + uniqueFileName);

        // Save the original file on the server
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // Apply mosaic processing to the image
        applyMosaic(originalFile, mosaicFile);

        // Save file path to the database (only the original image path)
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO images (img) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, UPLOAD_DIR + "/" + uniqueFileName);
                pstmt.executeUpdate();
            }
            response.sendRedirect("display.jsp");
        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timeStamp + "_" + originalFileName;
    }

    private void createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // Create directories if they do not exist
        }
    }

    private void applyMosaic(File inputFile, File outputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        int mosaicSize = 50; // Mosaic block size

        for (int y = 0; y < image.getHeight(); y += mosaicSize) {
            for (int x = 0; x < image.getWidth(); x += mosaicSize) {
                // Get the average color of the block
                int avgColor = getAverageColor(image, x, y, mosaicSize);
                // Fill the block with the average color
                for (int dy = 0; dy < mosaicSize && (y + dy) < image.getHeight(); dy++) {
                    for (int dx = 0; dx < mosaicSize && (x + dx) < image.getWidth(); dx++) {
                        image.setRGB(x + dx, y + dy, avgColor);
                    }
                }
            }
        }

        ImageIO.write(image, "png", outputFile);
    }

    private int getAverageColor(BufferedImage image, int x, int y, int size) {
        long r = 0, g = 0, b = 0;
        int count = 0;

        for (int dy = 0; dy < size && (y + dy) < image.getHeight(); dy++) {
            for (int dx = 0; dx < size && (x + dx) < image.getWidth(); dx++) {
                int color = image.getRGB(x + dx, y + dy);
                r += (color >> 16) & 0xFF;
                g += (color >> 8) & 0xFF;
                b += color & 0xFF;
                count++;
            }
        }

        r /= count;
        g /= count;
        b /= count;

        return (int) ((r << 16) | (g << 8) | b);
    }
}
