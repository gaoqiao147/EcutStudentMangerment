package com.ecut.test;

import lombok.ToString;

/**
 * @Author: 周伟
 * @CreateTime: 2023-01-05  09:58
 * @Version: 1.0
 */
public class ConstructionTest {
    public static void main(String[] args) {
        Cat cat = new Cat("大熊猫", "团团", "3");
        System.out.println(cat);
    }
}
     class Animal {
        public String species;

        public Animal(String species) {
            this.species = species;
        }
    }

     class Cat extends Animal {
        private String name;
        private String age;

        public Cat(String species, String name, String age) {
            super(species);
            this.name = name;
            this.age = age;
        }

         @Override
         public String toString() {
             return "Cat{" +
                     "speries='" +  species +'\'' +
                     "name='" + name + '\'' +
                     ", age='" + age + '\'' +
                     '}';
         }
     }

