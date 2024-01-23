package ru.gb.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Car {
    private String model = "2101";
    private String made = "Lada";
    @Autowired
    private Engine carEngine;

    public Car(Engine engine) {
        this.carEngine = engine;
        System.out.println("Автомобиль создан");
    }

    public Engine getCarEngine() {
        return carEngine;
    }

    public void setCarEngine(Engine carEngine) {
        this.carEngine = carEngine;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", made='" + made + '\'' +
                ", carEngine=" + carEngine +
                '}';
    }
}
