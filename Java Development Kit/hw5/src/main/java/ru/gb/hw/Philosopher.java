package ru.gb.hw;

import ru.gb.old_hw.Fork;
import ru.gb.old_hw.Table;

public class Philosopher implements Runnable, Eater {
    int ateThrice;
    int seaterNumber;
    private final String name;


    private ru.gb.old_hw.Fork leftFork;
    private ru.gb.old_hw.Fork rightFork;

    public Philosopher(String name, int timesToEat) {
        this.name = name;

    }

    public void takeAFork(ru.gb.old_hw.Fork fork) {
        System.out.println(this.name + " берёт вилку " + fork.getForkNumber() + " (вилка уже взята: " + fork.isTaken() + ")");
        fork.setFork(true);
    }

    public void putDownAFork(Fork fork) {
        System.out.println(this.name + " положил вилку " + fork.getForkNumber());
        fork.setFork(false);
    }

    public void toEat() throws InterruptedException {
        seaterNumber = ru.gb.old_hw.Table.seaters.indexOf(this);
        leftFork = ru.gb.old_hw.Table.forks.get(seaterNumber);
        rightFork = ru.gb.old_hw.Table.forks.get((seaterNumber + 1) % Table.seaters.size());

            if (!leftFork.isTaken()) {
                takeAFork(leftFork);
                if (!rightFork.isTaken()) {
                    takeAFork(rightFork);
                  //  sleep(100);
                    System.out.println(name + " поел!");
                    ateThrice++;
                        }
                    }
//                }
//            }
        {
            toPonder();
        }
        if (leftFork.isTaken()) putDownAFork(leftFork);
        if (rightFork.isTaken()) putDownAFork(rightFork);
    }

    @Override
    public void run() {
        while (ateThrice < 3) {
            try {
                toEat();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(this.name + " наелся!");
    }

    public void toPonder() throws InterruptedException {
     //   sleep(1000);
        System.out.println(name + " поразмышлял!");
    }

    @Override
    public void assign(ru.gb.hw.Fork left, ru.gb.hw.Fork right) {

    }
}
