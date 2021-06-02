package com.capstone.files.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

/**
 * 파일 다운 컨트롤러
 * 
 * @since 2021.05.26
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
public class FileController {

	@Autowired
	ResourceLoader resourceLoader;

	/**
	 * 파일 업로드 페이지
	 * 
	 * @param model
	 * @return
	 */
//	@RequestMapping("/argos/files/getFileUpload")
//	public String getFileUpload(Model model) {
//		return "argos/files/viewFileUpload";
//	}
//	
//	@PostMapping("/argos/files/fileUpload")
//	public String fileUpload(Model model, @RequestParam("file") MultipartFile files) {
//		try {
//            String filename = "Argos.zip";
//            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
//            String savePath = "/opt/tomcat-least/webapps/files/";
//            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
//            if (!new File(savePath).exists()) {
//                try{
//                    new File(savePath).mkdir();
//                }
//                catch(Exception e){
//                    e.getStackTrace();
//                }
//            }
//            String filePath = savePath + filename;
//            files.transferTo(new File(filePath));
//            
//            return "redirect:/argos/files/getFileUpload";
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//		return "redirect:/main";
//	}
	
	@RequestMapping("/getArgosProgram")
	@ResponseStatus(HttpStatus.OK)	// Thymeleaf 사용시 이것을 사용해야 에러가 발생하지 않음
	public void fileDownloadOnWebBroweser(HttpServletRequest req, HttpServletResponse res) throws Exception  {

		String savePath = "/opt/tomcat-least/webapps/files/Argos.zip";
	 	File f = new File(savePath);	//파일이 없는 경우 fileNotFoundException error가 난다.

	 	String downloadName = null;
		String browser = req.getHeader("User-Agent");
		//파일 인코딩
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){
		  //브라우저 확인 파일명 encode  		             
		  downloadName = URLEncoder.encode(f.getName(), "UTF-8").replaceAll("\\+", "%20");		             
		}else{		             
		  downloadName = new String(f.getName().getBytes("UTF-8"), "ISO-8859-1");
		
		}        
		res.setHeader("Content-Disposition", "attachment;filename=\"" + downloadName +"\"");             
		res.setContentType("application/octer-stream");
		res.setHeader("Content-Transfer-Encoding", "binary;");

		try(
			FileInputStream fis = new FileInputStream(f);
			ServletOutputStream sos = res.getOutputStream();	
		) {

			byte[] b = new byte[1024];
    		int data = 0;

      	while((data=(fis.read(b, 0, b.length))) != -1){		             
      		sos.write(b, 0, data);		             
      	}

      		sos.flush();
    	} catch(Exception e) {
    		throw e;
    	}
    }

}
