package com.pruebas.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String UPLOADS_FOLDER = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = this.getPath(filename);
		logger.info(String.format("pathFoto: %s", pathFoto));

		Resource recurso = new UrlResource(pathFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException(String.format("Error: no se puede cargar la imagen: %s", pathFoto.toString()));
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFilename = String.format("%s_%s", UUID.randomUUID().toString(), file.getOriginalFilename());
		Path rootPath = this.getPath(uniqueFilename);

		logger.info(String.format("rootPath: %s", rootPath));

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = this.getPath(filename);
		File archivo = rootPath.toFile();
		Boolean res = false;

		if (archivo.exists() && archivo.canRead())
			if (archivo.delete())
				res = true;
		return res;
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(this.UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(this.UPLOADS_FOLDER));
	}

	public Path getPath(String filename) {
		return Paths.get(this.UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

}
