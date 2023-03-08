package com.ecut.test;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;


class Love {
    int x;
    int y;
    int size;
    float alpha;
    Timer timer;
    JPanel heart;
    @Test
    public static void main(String[] args) {
        new Love();
    }

    public Love() {
        x = 0;
        y = 0;
        size = 40;
        alpha = 0.0f;

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.2f;
                if (alpha > 1.0f)
                    alpha = 0.0f;

                heart.repaint();
            }
        });
        timer.start();

        JFrame frame = new JFrame("Love");
        heart = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.setColor(Color.pink);
                drawHeart(g2d);
            }

            public void drawHeart(Graphics2D g2d) {
                int xPoints[] = {x + 25, x + 35, x + 45, x + 25, x + 5, x + 15, x + 25};
                int yPoints[] = {y + 25, y + 5, y + 25, y + 45, y + 45, y + 25, y + 5};
                GeneralPath heart = new GeneralPath();
                heart.moveTo(xPoints[0], yPoints[0]);
                for (int i = 1; i < xPoints.length; i++) {
                    heart.lineTo(xPoints[i], yPoints[i]);
                }
                heart.closePath();
                g2d.fill(heart);
            }
        };
        frame.add(heart);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(50, 50);
        frame.setVisible(true);
    }
}