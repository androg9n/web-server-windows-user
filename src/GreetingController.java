package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Controller
public class GreetingController {

    @RequestMapping(value = { "/greeting" }, method = RequestMethod.GET)
    public String greeting(Model model) {
        UserName userName = new UserName();
        model.addAttribute("userName", userName);
        System.out.println("greeting get");
        return "greeting";
    }

    @RequestMapping(value = { "/greeting" }, method = RequestMethod.POST)
    public String testUser(Model model, //
                             @ModelAttribute("userName") UserName userName) {
        String result;
        if (isUserPresent(userName.getName())) result = "Пользователь есть"; else result = "Пользователя нет";

            userName.setName(result);

        model.addAttribute("testAnswer", userName);
        System.out.println("greeting post | testUserName: " + userName.getName());
        return "answer";
    }

    @RequestMapping(value = { "/answer" }, method = RequestMethod.POST)
    public String answerPost(Model model) {
        UserName userName = new UserName();
        model.addAttribute("userName", userName);
        System.out.println("answer post ");
        return "greeting";
    }

    private boolean isUserPresent(String testUserName) {
        //Load user list
        ProcessBuilder processBuilder = new ProcessBuilder("net","user");
        processBuilder.redirectErrorStream(true);
        String output = runProcessAndReturnOutput(processBuilder);
        //Check if user is in list
        //We assume the output to be a list of users with the net user
        //Remove long space sequences
        output = output.replaceAll("\\s+", " ").toLowerCase();
        //Locate user name in resulting list
        String[] tokens = output.split(" ");
        Arrays.sort(tokens);
        if (Arrays.binarySearch(tokens, testUserName.toLowerCase()) >= 0){
            //We found the user name
            return true;
        }
        return false;
    }

    private String runProcessAndReturnOutput(ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "CP866");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder stringBuilder = new StringBuilder();
            String s;
            boolean lines = false;
            while ((s = br.readLine()) != null) {
                if (s.startsWith("Команда выполнена успешно")) lines = false;
                if (lines) {
                    stringBuilder.append(s);
                    System.out.println(s);
                }
                if (s.startsWith("-------")) lines = true;
            }
            br.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
