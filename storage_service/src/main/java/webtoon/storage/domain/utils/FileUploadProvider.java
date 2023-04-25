package webtoon.storage.domain.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import webtoon.storage.domain.entities.FileEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@Component
public class FileUploadProvider {

	public static String DOMAIN_HOST;
	public static String ROOT_CONTENT_SYS;

	FileUploadProvider(@Value("${app.root-content-path}") String ROOT_CONTENT_SYS, @Value("${app.domain}") String HOST) {
		FileUploadProvider.DOMAIN_HOST = HOST;
		FileUploadProvider.ROOT_CONTENT_SYS = ROOT_CONTENT_SYS;
	}

	public FileEntity uploadFile(MultipartFile file) {
		Calendar calendar = Calendar.getInstance();
		StringBuilder datePath = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("/")
				.append(calendar.get(Calendar.MONTH) + 1).append("/");
		return this.uploadFile(file, datePath.toString());
	}

	public void deleteFile(String url) {
		File file = new File(url.replace(FileUploadProvider.DOMAIN_HOST, FileUploadProvider.ROOT_CONTENT_SYS));
		if (file.exists())
			file.delete();
	}

	public FileEntity uploadFile(MultipartFile file, String path) {
		File checkPath = new File(FileUploadProvider.ROOT_CONTENT_SYS+path);
		if (!checkPath.exists())
			checkPath.mkdirs();

		StringBuilder pathFile = new StringBuilder(FileUploadProvider.ROOT_CONTENT_SYS).append(path);
		pathFile.append(file.getOriginalFilename());

		File checkFile = checkFileExist(pathFile.toString());
		int i = 0;
		StringBuilder fileName = new StringBuilder(file.getOriginalFilename());
		while (checkFile == null) {
			pathFile.setLength(0);

			fileName.setLength(0);
			fileName.append(i++).append("-").append(file.getOriginalFilename());

			pathFile.append(FileUploadProvider.ROOT_CONTENT_SYS).append(path).append(i++).append("-")
					.append(file.getOriginalFilename());
			checkFile = checkFileExist(pathFile.toString());
		}

		try {
			file.transferTo(new File(pathFile.toString()));
		} catch (IOException e) {
//            throw new CustomHandleException(72);
		}

		String domainPath = pathFile.replace(0, FileUploadProvider.ROOT_CONTENT_SYS.length(), FileUploadProvider.DOMAIN_HOST)
				.toString();

		FileEntity fileEntity = FileEntity.builder().fileName(fileName.toString()).fileType(file.getContentType())
				.url(domainPath).title(file.getOriginalFilename()).alt(file.getOriginalFilename()).createdBy(1L)
				.build();
		return fileEntity;
	}

	public FileEntity uploadFile(byte[] bytes, String folder, String fileName) throws IOException {
		File folderCheck = new File(FileUploadProvider.ROOT_CONTENT_SYS + folder);
		if (!folderCheck.exists())
			folderCheck.mkdirs();

		String filePath = folder + fileName;
		File newFile = new File(filePath);
		FileOutputStream fos = new FileOutputStream(FileUploadProvider.ROOT_CONTENT_SYS + newFile);
		fos.write(bytes);
		fos.close();
		FileEntity fileEntity = FileEntity.builder().url(FileUploadProvider.DOMAIN_HOST + filePath).createdBy(1L).build();
		return fileEntity;

	}

	private File checkFileExist(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return null;
		return file;
	}

}
