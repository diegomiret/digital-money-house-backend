package com.dh.digitalMoneyHouse.usersservice.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
@Service
public class AliasCvuGenerator {

    private Random random = new Random();

    public String generateAlias() {

        List<String> aliasOptions = new ArrayList<String>();
        List<String> chosenAlias = new ArrayList<>();

        try {
            //File aliasFile = new File("/app/utils/alias.txt");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("alias.txt");

            //notebook: "/Users/pablomerino/Desktop/DigitalMoneyHouse/users-service/src/main/java/com/dh/digitalMoneyHouse/usersservice/utils/alias.txt"
            //Scanner myReader = new Scanner(aliasFile);
            Scanner myReader = new Scanner(inputStream);

            while (myReader.hasNextLine()) {
                String word = myReader.nextLine();
                aliasOptions.add(word);
            }

            myReader.close();

            for (int i = 0; i < 3; i++) {
                int randomPosition;
                String randomAlias;

                do {
                    randomPosition = random.nextInt(aliasOptions.size());
                    randomAlias = aliasOptions.get(randomPosition);
                } while (chosenAlias.contains(randomAlias));

                chosenAlias.add(randomAlias);
            }

            return String.join(".", chosenAlias);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String generateCvu() {

        StringBuilder cvu= new StringBuilder("7031990");

        for(int i=0; i<15; i++) {
            int randomNumber = random.nextInt(10);
            cvu.append(randomNumber);
        }

         return cvu.toString();
    }
}

