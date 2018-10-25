package com.assess;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "src\\main\\resources\\image\\";
    private static String REPLACED_FOLDER = "src\\main\\resources\\archive\\";


    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/uploadImage")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Path setPath = Paths.get(REPLACED_FOLDER + file.getOriginalFilename());
            
            if(Files.exists(path)) {
            	Files.move(path, setPath, StandardCopyOption.REPLACE_EXISTING);
                Files.write(path, bytes);

            	redirectAttributes.addFlashAttribute("message",
                        "File name already exists with '" + file.getOriginalFilename() + "' name. "+"The older file has been moved to the Archives folder  ");
            }else {
                Files.write(path, bytes);
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'"+"and the file size is  "+file.getSize());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
    

}