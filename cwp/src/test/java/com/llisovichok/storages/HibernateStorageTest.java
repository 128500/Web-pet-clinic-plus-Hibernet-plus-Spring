package com.llisovichok.storages;

import com.llisovichok.lessons.clinic.Pet;
import com.llisovichok.lessons.clinic.PetPhoto;
import com.llisovichok.models.Message;
import com.llisovichok.models.Role;
import com.llisovichok.models.User;
import junit.framework.AssertionFailedError;

import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by KUDIN ALEKSANDR on 16.07.2017.
 */
public class HibernateStorageTest {

    private final static HibernateStorage H_STORAGE = HibernateStorage.getInstance();
    private final static Message m1 = new Message("first");
    private final static Message m2 = new Message("second");
    private final static Set<Message> messages = new HashSet<>();
    private final static String URL_PATH = "http://www.avajava.com/images/avajavalogo.jpg";
    private final static byte [] imageBytes;

    static {
       imageBytes = getImageBytes(URL_PATH);
    }
    private User createUser1() {
        User u = new User("Test", "Test", "Test", 1201251455454L,
                new Pet("Test", "test",3));
        u.setRole(new Role ("user"));
        return u;
    }

    private User createUser2() {
        User u = new User("Gordon", "Dannison", "Test", 11111111111L,
                new Pet("Harpy", "harpy", 5));
        u.setRole(new Role("admin"));
        return u;
    }

    private boolean checkUser1(User user) throws AssertionFailedError {
        assertTrue(user.getFirstName().equals("Test"));
        assertTrue(user.getLastName().equals("Test"));
        assertTrue(user.getAddress().equals("Test"));
        assertTrue(user.getPhoneNumber() == 1201251455454L);
        assertTrue(user.getPet().getName().equals("Test"));
        assertTrue(user.getPet().getKind().equals("test"));
        assertTrue(user.getPet().getAge() == 3);
        assertTrue(user.getRole().getName().equals("user"));
        for (Message m : user.getMessages()) {
            assertTrue(m.getText().equals("first") || m.getText().equals("second"));
        }

        return true;
    }

    private boolean checkChangedUser(User user) throws AssertionFailedError {
        assertTrue(user.getFirstName().equals("Gordon"));
        assertTrue(user.getLastName().equals("Dannison"));
        assertTrue(user.getAddress().equals("Test"));
        assertTrue(user.getPhoneNumber() == 11111111111L);
        assertTrue(user.getPet().getName().equals("Harpy"));
        assertTrue(user.getPet().getKind().equals("harpy"));
        assertTrue(user.getPet().getAge() == 5);
        assertTrue(user.getRole().getName().equals("admin"));
        for (Message m : user.getMessages()) {
            assertTrue(m.getText().equals("first") || m.getText().equals("second"));
        }

        return true;
    }

    private static byte[] getImageBytes(final String urlPath){
        InputStream is = null;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            URL url = new URL(urlPath);
            is  = url.openStream();
            byte[] data = new byte[1024];
            int nRead = 0;

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                baos.write(data, 0, nRead);
            }

            baos.flush();
            int byteArraySize = baos.size();

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(is != null) is.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        throw new IllegalStateException("Couldn't  get image bytes");
    }

    @Test
    public void getInstance() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || getInstance()");
        System.out.println("\n=========================================================================================");

        assertTrue(H_STORAGE != null);

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void getFactory() throws Exception{
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || getFactory()");
        System.out.println("\n=========================================================================================");

        assertTrue(H_STORAGE.getFactory() != null);
    }

    @Test
    public void values() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || values()");
        System.out.println("\n=========================================================================================");

        int [] ids = new int[5];
        for(int i = 0; i < 5; i++){

            User user = createUser1();

            final int id = H_STORAGE.addUser(user);
            ids[i] = id;
            User retrieved  = H_STORAGE.getUser(id);

            m1.setUser(retrieved);
            m2.setUser(retrieved);

            messages.add(m1);
            messages.add(m2);
            retrieved.setMessages(messages);

            H_STORAGE.editUser(id, retrieved);
        }

        ArrayList<User> users = (ArrayList<User>) H_STORAGE.values();
        assertTrue(users.size() >= 5);
        for(Integer number : ids){
            assertEquals(true, checkUser1(H_STORAGE.getUser(number)));
        }
        System.out.println("\n=========================================================================================");
    }


    @Test
    public void addUser() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || addUser()");
        System.out.println("\n=========================================================================================");

        Integer id  = H_STORAGE.addUser(this.createUser1());
        System.out.println("Id : " + id);
        assertTrue(id > 0);

        User retrieved  = H_STORAGE.getUser(id);

        m1.setUser(retrieved);
        m2.setUser(retrieved);

        messages.add(m1);
        messages.add(m2);
        retrieved.setMessages(messages);

        H_STORAGE.editUser(id, retrieved);

        retrieved  = H_STORAGE.getUser(id);

        assertEquals(true, this.checkUser1(retrieved));

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void addUserWithPhotoAndMessages() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || addUserWithPhotoAndMessages()");
        System.out.println("\n=========================================================================================");

        User user = this.createUser1();
        Message m = new Message("first");
        Message mes = new Message("second");
        Set<Message> mess = new HashSet<>();
        mess.add(m);
        mess.add(mes);
        user.setMessages(mess);
        PetPhoto photo = new PetPhoto(imageBytes);
        user.getPet().setPhoto(photo);

        Integer userId = H_STORAGE.addUser(user);

        User retrieved = H_STORAGE.getUser(userId);

        assertEquals(true, this.checkUser1(retrieved));

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void edit() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || edit()");
        System.out.println("\n=========================================================================================");

        Integer id = H_STORAGE.addUser(this.createUser1());

        User retrieved = H_STORAGE.getUser(id);

        assertTrue(retrieved.getMessages().isEmpty());
        assertEquals(true, this.checkUser1(retrieved));

        retrieved.setFirstName("Gordon");
        retrieved.setLastName("Dannison");
        retrieved.setAddress("Test");
        retrieved.setPhoneNumber(11111111111L);
        retrieved.getPet().setName("Harpy");
        retrieved.getPet().setKind("harpy");
        retrieved.getPet().setAge(5);
        retrieved.setRole(new Role("admin"));

        m1.setUser(retrieved);
        m2.setUser(retrieved);

        messages.add(m1);
        messages.add(m2);
        retrieved.setMessages(messages);

        boolean check = H_STORAGE.editUser(id, retrieved);
        assertTrue(check);
        retrieved  = H_STORAGE.getUser(id);
        assertEquals(true, checkChangedUser(retrieved));

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void getUser() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || getUser()");
        System.out.println("\n=========================================================================================");

        Integer id = H_STORAGE.addUser(this.createUser2());

        User retrieved  = H_STORAGE.getUser(id);

        m1.setUser(retrieved);
        m2.setUser(retrieved);

        messages.add(m1);
        messages.add(m2);
        retrieved.setMessages(messages);

        H_STORAGE.editUser(id, retrieved);

        retrieved  = H_STORAGE.getUser(id);

        assertEquals(true, checkChangedUser(retrieved));

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void removeUser() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || removeUser()");
        System.out.println("\n=========================================================================================");

        ArrayList<User> users_before = (ArrayList<User>)H_STORAGE.values();

        Set<Message> mes = new HashSet<>();
        mes.add(new Message("1"));
        mes.add(new Message("2"));
        mes.add(new Message("3"));
        User user_1 = createUser1();
        user_1.setMessages(mes);

        Set<Message> mes2 = new HashSet<>();
        mes2.add(new Message("55"));
        User user_2 = createUser2();
        user_2.setMessages(mes2);
        Integer id1 = H_STORAGE.addUser(user_1);
        Integer id2 = H_STORAGE.addUser(user_2);

        ArrayList<User> users_current = (ArrayList<User>)H_STORAGE.values();
        assertTrue(users_current.size() > users_before.size());
        H_STORAGE.removeUser(id1);
        H_STORAGE.removeUser(id2);
        ArrayList<User> users_after = (ArrayList<User>)H_STORAGE.values();
        assertTrue(users_after.size() == users_before.size());

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void removeUserWithPetPhoto() throws Exception{
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || removeUserWithPetPhoto()");
        System.out.println("\n=========================================================================================");

        Integer userId = H_STORAGE.addUser(createUser1());

        User user = H_STORAGE.getUser(userId);

        Integer petId = user.getPet().getId();

        H_STORAGE.addPhotoWithHibernate(petId, imageBytes);

        Set<Message> mes = new HashSet<>();
        mes.add(new Message("1"));
        mes.add(new Message("2"));
        mes.add(new Message("3"));

        user.setMessages(mes);

        H_STORAGE.editUser(userId, user);

        user = H_STORAGE.getUser(userId);

        H_STORAGE.removeUser(userId);

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void findUsers() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || findUsers()");
        System.out.println("\n=========================================================================================");

        User gordon = createUser1();
        gordon.setFirstName("Sam");
        gordon.setLastName("Edison");
        gordon.getPet().setName("Darko");
        gordon.setAddress("Fifth av.");

        User don = createUser1();
        don.setFirstName("Sam");
        don.setLastName("Ramsy");
        don.getPet().setName("Darko");
        don.setAddress("Fifth av.");

        User bob = createUser1();
        bob.setFirstName("Bob");
        bob.setLastName("Ramsy");
        bob.getPet().setName("Diana");
        bob.setAddress("Passadina st.");

        H_STORAGE.addUser(gordon);
        H_STORAGE.addUser(don);
        H_STORAGE.addUser(bob);

        /* Searching in users' first names */
        /* Two matches found*/
        ArrayList<User> users = (ArrayList<User>) H_STORAGE.findUsers("sam", true, false, false);
        assertTrue(users.size() == 2);
        for(User u : users){
            assertTrue(u.getFirstName().equals("Sam"));
        }

        /* One match found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("Bob", true, false, false);
        assertTrue(users.size() == 1);
        assertTrue(users.get(0).getFirstName().equals("Bob"));

        /* No matches found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("111", true, false, false);
        assertTrue(users.size() == 0);


        /* Searching in users' last names*/
        /* Two matches found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("Ramsy", false, true, false);
        assertTrue(users.size() == 2);
        for(User u : users){
            assertTrue(u.getLastName().equals("Ramsy"));
        }

        /* One match found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("Edison", false, true, false);
        assertTrue(users.size() == 1);
        assertTrue(users.get(0).getLastName().equals("Edison"));

        /* No matches found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("111", false, true, false);
        assertTrue(users.size() == 0);



        /* Searching in pets' names*/
        /* Two matches found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("Darko", false, false, true);
        assertTrue(users.size() == 2);
        for(User u : users){
            assertTrue(u.getPet().getName().equals("Darko"));
        }

        /* One match found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("Diana", false, false, true);
        assertTrue(users.size() == 1);
        assertTrue(users.get(0).getPet().getName().equals("Diana"));

        /* No matches found*/
        users = (ArrayList<User>) H_STORAGE.findUsers("111", false, false, true);
        assertTrue(users.size() == 0);



        /* Searching in addresses */
        /* Two matches found */
        users = (ArrayList<User>) H_STORAGE.findUsers("Fifth av.", false, false, false);
        assertTrue(users.size() == 2);
        for(User u : users){
            assertTrue(u.getAddress().equals("Fifth av."));
        }

        /* One match found */
        users = (ArrayList<User>) H_STORAGE.findUsers("Passadina st.", false, false, false);
        assertTrue(users.size() == 1);
        assertTrue(users.get(0).getAddress().equals("Passadina st."));

        /* No matches found */
        users = (ArrayList<User>) H_STORAGE.findUsers("111", false, false, false);
        assertTrue(users.size() == 0);

        System.out.println("\n=========================================================================================");
    }


    @Test
    public void findUsersIfNoUsersFound() throws Exception{
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || findUsersIfNoUsersFound()");
        System.out.println("\n=========================================================================================");

        assertTrue(H_STORAGE.findUsers("1111111", true, true, true).isEmpty());

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void getPetById() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || getPetById()");
        System.out.println("\n=========================================================================================");

        Integer id = H_STORAGE.addUser(createUser2());
        Integer petId = H_STORAGE.getUser(id).getPet().getId();
        System.out.println("Id " + petId);
        H_STORAGE.addPhotoWithHibernate(petId, imageBytes);
        Pet pet = H_STORAGE.getPetById(petId);
        assertEquals("Harpy", pet.getName());
        assertEquals("harpy", pet.getKind());
        assertEquals(Integer.valueOf(5), pet.getAge());
        assertTrue(pet.getPhoto() != null);
        System.out.println("\n=========================================================================================");

    }

    @Test
    public void addPhoto() throws Exception{
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || addPhoto()");
        System.out.println("\n=========================================================================================");

        try(ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)){

            Integer id = H_STORAGE.addUser(this.createUser1());
            H_STORAGE.addPhoto(id, bais, imageBytes.length);

        } catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("\n=========================================================================================");
    }

    @Test
    public void addPhotoWithHibernate() throws Exception {
        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || addPhotoWithHibernate()");
        System.out.println("\n=========================================================================================");

        Integer id = H_STORAGE.addUser(createUser1());

        User user = H_STORAGE.getUser(id);

        Integer petId = user.getPet().getId();

        H_STORAGE.addPhotoWithHibernate(petId, imageBytes);

        Pet pet  = H_STORAGE.getPetById(petId);

        assertTrue(imageBytes.length == pet.getPhoto().getImage().length);

        System.out.println("\n=========================================================================================");

    }

    @Test
    public void addMessage() throws Exception {

        System.out.println("\n=========================================================================================");
        System.out.println("Testing  || " + this.getClass().getName() + " || addMessage()");
        System.out.println("\n=========================================================================================");

        User user = createUser1();
        Integer id = H_STORAGE.addUser(user);

        H_STORAGE.addMessage(id, "First message");

        user = H_STORAGE.getUser(id);
        assertEquals("First message", user.getMessages().iterator().next().getText());

        H_STORAGE.addMessage(id, "Second message");
        user = H_STORAGE.getUser(id);

        for(Message m : user.getMessages()){
            assertTrue(m.getText().equals("First message") || m.getText().equals("Second message"));
        }
    }
}