package com.llisovichok.controllers;

import com.llisovichok.lessons.clinic.Pet;
import com.llisovichok.models.User;
import com.llisovichok.storages.Storages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KUDIN ALEKSANDR on 23.10.2017.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Storages storages;

    @ModelAttribute("user")
    public User getUserModel(){
        return new User();
    }

    @RequestMapping(value="/create_account", method = RequestMethod.GET)
    public ModelAndView createAccount(){
         return new ModelAndView("user/create_account");
    }


    @RequestMapping(value = "/add_user", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute("user") User user){
        storages.shHiberStorage.addUser(user);
        return new ModelAndView("redirect:/admin/view_users");
    }

    @RequestMapping(value = "/addinfo/{id}", method = RequestMethod.GET )
    public String addInfo(@PathVariable("id") Integer id, ModelMap model){
        model.addAttribute("user", storages.shHiberStorage.getUser(id));
        return "/user/add_info";
    }

    @RequestMapping(value = "/save_photo", method = RequestMethod.POST)
    public String addPhoto(@RequestParam("pet_id") Integer petId,
                           @RequestParam("photo") MultipartFile file,
                           ModelMap model){
        byte [] photoBytes = getPhotoBytes(file); // see below for method definition
        boolean result = storages.shHiberStorage.addPhotoWithHibernate(petId, photoBytes);
        if(result) model.addAttribute("message", "PHOTO WAS SUCCESSFULLY ADDED");
        else model.addAttribute("message", "COULDN'T ADD PHOTO");
        return "/user/progress_in_adding_photo";
    }


    /**
     * Retrieves an array of bytes from the given MultipartFile
     * @param file - MultipartFile
     * @return array of retrieved bytes of photo
     */
    private byte [] getPhotoBytes(final MultipartFile file){
        try{
            byte[] data = new byte[1024];
            int nRead;

            try(InputStream fileContent = file.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                while ((nRead = fileContent.read(data, 0, data.length)) != -1) {
                    baos.write(data, 0, nRead);
                }
                baos.flush();

                return baos.toByteArray();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        throw new IllegalStateException("Couldn't image bytes");
    }
}
