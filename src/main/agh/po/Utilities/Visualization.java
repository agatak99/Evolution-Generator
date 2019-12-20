package agh.po.Utilities;

import agh.po.Map.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualization {
    JFrame frame;
    Timer timer;
    JTextArea textArea;
    JButton startStopButton;
    JButton stopSimulationButton;
    Simulation simulation;


    public Visualization(Simulation simulation) {
        this.init();
        this.simulation = simulation;
    }

    private void init() {
        this.frame = new JFrame("Animals simulation");
        frame.setSize(1600, 1200);
        frame.setLayout(null);
        frame.setVisible(true);

        this.textArea = new JTextArea("");
        textArea.setBounds(10, 60, 1500, 900);
        textArea.setEditable(false);

        this.startStopButton = new JButton("");
        startStopButton.setBounds(10, 10, 80, 20);
        frame.add(startStopButton);

        startStopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (timer == null)
                    startAnimation();
                else
                    stopAnimation();
            }
        });

        this.stopSimulationButton = new JButton("Zakończ");
        stopSimulationButton.setBounds(10, 30, 80, 20);
        stopSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        frame.add(startStopButton);
        frame.add(stopSimulationButton);

    }

    ActionListener timerListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
             if(!simulation.areAnimalsAlive()){
             textArea.setText(simulation.toString());
             startStopButton.setVisible(false);
             timer.stop();
             }
            textArea.setText(simulation.toString());
            frame.add(textArea);
            SwingUtilities.updateComponentTreeUI(frame);
            simulation.simulate();
        }
    };

    public void startAnimation() {
        if (timer == null) {
            timer = new Timer(1000, timerListener);
            timer.start();
            startStopButton.setText("Pauza");
        }
    }

    private void stopAnimation() {
        if (timer != null) {
            timer.stop();
            timer = null;
            startStopButton.setText("Start");
        }
    }
}
