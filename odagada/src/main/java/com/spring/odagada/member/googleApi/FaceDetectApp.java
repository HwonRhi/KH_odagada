package com.spring.odagada.member.googleApi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Vertex;
import com.google.common.collect.ImmutableList;

@SuppressWarnings("serial")
public class FaceDetectApp {
	
  private static final String APPLICATION_NAME = "Google-VisionFaceDetectSample/1.0";

  private static final int MAX_RESULTS = 7;
 
  public static void main(String[] args) throws IOException, GeneralSecurityException {
    if (args.length != 2) {
      System.err.println("Usage:");
      System.err.printf(
          "\tjava %s inputImagePath outputImagePath\n",
          FaceDetectApp.class.getCanonicalName());
      System.exit(1);
    }
    Path inputPath = Paths.get(args[0]);

		FaceDetectApp app = new FaceDetectApp(getVisionService());
		List<FaceAnnotation> faces = app.detectFaces(inputPath, MAX_RESULTS);
		System.out.printf("Found %d face%s\n", faces.size(), faces.size() == 1 ? "" : "s");
		//?????? 1??? ????????? ??? exception ??????. 
		if (faces.size() > 1) {
			throw new IndexOutOfBoundsException();
		}
	}
  
  
  public static Vision getVisionService() throws IOException, GeneralSecurityException {
    GoogleCredential credential =
        GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    
  }

  private final Vision vision;

  public FaceDetectApp(Vision vision) {
    this.vision = vision;
  }

  public List<FaceAnnotation> detectFaces(Path path, int maxResults) throws IOException {
    byte[] data = Files.readAllBytes(path);

    AnnotateImageRequest request =
        new AnnotateImageRequest()
            .setImage(new Image().encodeContent(data))
            .setFeatures(ImmutableList.of(
                new Feature()
                    .setType("FACE_DETECTION")
                    .setMaxResults(maxResults)));
    Vision.Images.Annotate annotate =
        vision.images()
            .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
    annotate.setDisableGZipContent(true);

    BatchAnnotateImagesResponse batchResponse = annotate.execute();
    assert batchResponse.getResponses().size() == 1;
    AnnotateImageResponse response = batchResponse.getResponses().get(0);
    if (response.getFaceAnnotations() == null) {
      throw new IOException(
          response.getError() != null
              ? response.getError().getMessage()
              : "Unknown error getting image annotations");
    }
    return response.getFaceAnnotations();
  }
}
