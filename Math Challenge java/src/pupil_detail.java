import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
/*question imports
package com.demo.ExcelProject;import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
package com.demo.ExcelProject;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

 * */



public class pupil_detail {
    //initialise pupil attributes
    private int participantId;
    private String name;
    private String username;
    private String email;
    private String password;
    private String date_of_birth;
    private String schoolRegNo;
    private String image;

   /* public static String hashPassword(String password, String salt) {
        byte[] saltedPassword = (password + salt).getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            saltedPassword = md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return Base64.getEncoder().encodeToString(saltedPassword);
    }

    // Method to generate a salt for hashing
    public static String generateSalt() {
        SecureRandom rnd = new SecureRandom();
        byte[] salt = new byte[16];
        rnd.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }*/

    //default constructor .Default constructor: Initializes an empty Pupil object.
    public pupil_detail() {
    }

    //constructor.Parameterized constructor: Initializes a Pupil object with the given parameters.
    public pupil_detail(String name, String username, String email, String password, String date_of_birth, String schoolRegNo, String image) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.schoolRegNo = schoolRegNo;
        this.image = image;
    }
    //Setters and Getters: Methods to set and get the values of the private fields.
    //Setters
    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public void setSchoolRegNo(String schoolRegNo) {
        this.schoolRegNo = schoolRegNo;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Getters
    public int getParticipantId() {
        return participantId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getSchoolRegNo() {
        return schoolRegNo;
    }

    public String getImage() {
        return image;
    }

    //Register new interested participant

    //gets printwriter in server to communicate directly to client
    public static void register(String[] req, PrintWriter out){
        if (req.length!=9){
            out.println("Missing parameters");
            out.println("");// helps break out of inner loop
        }else {
            String username = req[1];
            String name = req[2] +" "+ req[3];
            String email = req[4];
            String password = req[5];
            String dateOfBirth = req[6];
            String schoolRegNo = req[7];
            String imageFile = req[8];

            pupil_detail pupil = new pupil_detail(name, username, email, password, dateOfBirth, schoolRegNo, imageFile);
            if (!Model.checkRegNo(pupil)) {
                out.println("School not registered, please contact the system administrator to register your school first");
                out.println("");
            } else if (Model.checkUsername(pupil)) {
                out.println("Username already taken,try another one");
                out.println("");
            } else if (Model.checkStudentRegistration(pupil)) {
                out.println("Student already registered, please login to attempt challenges");
                out.println("");
            } else {
                pupil_detail.addPupilToFile(pupil);
                //call method to send email to school representative
                out.println("Wait for confirmation email from the system administrator");
                out.println("");
            }
        }
    }


    //Allows logged in participant to view open challenges
    public static void viewChallenges(PrintWriter printWriter){
        String chal=null;
        try(Connection conn = Model.createConnection();){

            String sql = "SELECT challengeNum, challenge_name from challenge";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                chal=rs.getString("challengeNum")+"."+rs.getString("challenge_name");
                printWriter.println(chal);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        };
    }
    /*
//will work later
    //Allows user to attempt a challenge they are interested in
    public static void attemptChallenge(PrintWriter printWriter, BufferedReader br, String[] req){

        try(Connection conn = Model.createConnection();){

            String sql = "SELECT ChallengeID from Challenge";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String tempID = rs.getString("ChallengeID");
                if(req[1].equals(tempID)){
                    Question.retrieveQuestion(printWriter, br);
                }
                else{
                    String error = "ChallengeID not recognised";
                    printWriter.println(error);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        };
    }*/

    //check if reg no supplied is in the database

    //to add a pupil to a file

    public static void addPupilToFile(pupil_detail pupil){
        //from applicants to pupils.txt......
        //Filewriter is a class in java.io package used to write character files.
        //buffered writer is a wrapper around Writer that provides buffering for improved efficiency
        //buffering can increase performance when writing large amouts of data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Hormisdallen.txt",true));
        )// BufferedWriter: Wraps the FileWriter in a BufferedWriter for efficient writing.
        //FileWriter: Creates a FileWriter object to write to the file.
        {
            writer.write(pupil.getName() + " " + pupil.getUsername() + " " + pupil.getEmail() + " " + pupil.getPassword() + " " + pupil.getDate_of_birth() + " " + pupil.getSchoolRegNo() + " " + pupil.getImage() + "\n");
//Writing to File: Writes the pupil's details to the file.
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    //put all applicants into an arraylist
    public static ArrayList<pupil_detail> addToArrayList(String username){
      //reads pupil details from applicants.txt and adds them to an ArrayList:
        ArrayList<pupil_detail> applicants = new ArrayList<>();
        //Creates an ArrayList to hold pupil_detail objects.
        String school = "pupils";
        String filename = school + ".txt";
        //BufferedReader: Reads the file line by line using a BufferedReader.
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
//Splits each line into parts and creates a pupil_detail object for each line.
            while ((line = br.readLine()) != null){
                String[] parts = line.trim().split(" ");
                pupil_detail pupil = new pupil_detail();
//Adding to List: Adds each pupil_detail object to the ArrayList.
                pupil.setName(parts[0]+ " " + parts[1]);
                pupil.setUsername(parts[2]);
                pupil.setEmail(parts[3]);
                pupil.setPassword(parts[4]);
                pupil.setDate_of_birth(parts[5]);
                pupil.setSchoolRegNo(parts[6]);
                pupil.setImage(parts[7]);
                applicants.add(pupil);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return applicants;
    }

    //to delete a pupil from the file after being confirmed or rejected
    public static void deleteFromFile(String username){
      //The deleteFromFile method deletes a pupil from the applicants.txt file:
        ArrayList<pupil_detail> applicants = addToArrayList(username);
        // Reads all pupils into an ArrayList using the addToArrayList method.
        String school = "pupils";
        String filename = school + ".txt";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename));) {
            for (pupil_detail pupil : applicants) {

                if (!pupil.getUsername().equals(username)) {
                    bw.write(pupil.getName() + " " + pupil.getUsername() + " " + pupil.getEmail() + " " + pupil.getPassword() + " " + pupil.getDate_of_birth() + " " + pupil.getSchoolRegNo() + " " + pupil.getImage() + "\n");
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    //to string
    public String toString() {
        return "Pupil{" +
                "participantId=" + participantId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", schoolRegNo='" + schoolRegNo + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
