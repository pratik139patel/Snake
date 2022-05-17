package src;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.awt.Toolkit;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;

public class Snake 
{
    private static JMenuItem start_game;
    private static JMenuItem stop_game;
    private static JMenuItem restart_game;
    private static JLabel game_score;
    private static SnakeGamePane gaming_pane;
    private static JFrame game_frame;


    public Snake() 
    {
        RetriveSettings();

        EventQueue.invokeLater(new Runnable() 
        {
            @Override public void run() 
            {
                try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) { ex.printStackTrace(); }

                game_frame = new JFrame("Snake"); game_frame.setIconImage(new ImageIcon("pratik-patel.jpeg").getImage());
                JMenuBar game_menu_bar = new JMenuBar();
                JMenu game_options = new JMenu("Game");
                JMenu game_settings = new JMenu("Settings");
                gaming_pane = new SnakeGamePane();

                JMenuItem exit_game = new JMenuItem("Exit"); exit_game.setAccelerator(KeyStroke.getKeyStroke('0', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); 
                start_game = new JMenuItem("Start"); start_game.setAccelerator(KeyStroke.getKeyStroke('1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); 
                stop_game = new JMenuItem("Stop"); stop_game.setAccelerator(KeyStroke.getKeyStroke('2', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                restart_game = new JMenuItem("Restart"); restart_game.setAccelerator(KeyStroke.getKeyStroke('3', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

                JMenuItem color_sett = new JMenuItem("Color"); game_settings.add(color_sett); color_sett.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                JMenuItem game_sett = new JMenuItem("Game Play"); game_settings.add(game_sett); game_sett.setAccelerator(KeyStroke.getKeyStroke('G', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                JMenuItem reset_sett = new JMenuItem("Reset All"); game_settings.add(reset_sett); reset_sett.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                JMenuItem relauch_sett = new JMenuItem("Re-Launch"); game_settings.add(relauch_sett); relauch_sett.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                JMenuItem credit_sett = new JMenuItem("Credits"); game_settings.add(credit_sett);

                game_options.add(start_game); game_options.add(stop_game); game_options.add(restart_game); game_options.add(exit_game);
                game_menu_bar.add(game_options); game_menu_bar.add(game_settings);
                game_frame.setJMenuBar(game_menu_bar);
                stop_game.setEnabled(false); restart_game.setEnabled(false);

                game_menu_bar.add(new JLabel("    SCORE: "));
                game_score = new JLabel("0");
                game_menu_bar.add(game_score);

                start_game.addActionListener(new ActionListener() 
                { 
                    @Override public void actionPerformed(ActionEvent e) 
                    { 
                        gaming_pane.Start(); start_game.setEnabled(false);stop_game.setEnabled(true); restart_game.setEnabled(true); 
                        if (preferred_dim.width == food_location.width && preferred_dim.height == food_location.height) 
                        {
                            Dimension temp_null_dim; 
                            if(smooth_motion) {temp_null_dim = new Dimension(obj_height_width,0);}
                            else {temp_null_dim = new Dimension(0,0);} 
                            do {gaming_pane.SpawnFood();} while(food_location.width == temp_null_dim.width && food_location.height == temp_null_dim.height); 
                        } 
                    } 
                });
                stop_game.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.Stop(); stop_game.setEnabled(false); start_game.setEnabled(true); restart_game.setEnabled(true); } });
                restart_game.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.Reset(); start_game.setEnabled(true); start_game.doClick(); } });
                color_sett.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { stop_game.doClick(); ColorSettMenu(); } });
                game_sett.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { stop_game.doClick(); GameSettMenu(); } });
                reset_sett.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { exit_game.doClick(); ResetDefaultSettings(true); Re_Launch(); } });
                relauch_sett.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { exit_game.doClick(); Re_Launch(); } });
                credit_sett.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { stop_game.doClick(); credit_menu(); } });
                exit_game.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.Stop(); game_frame.dispose(); try{game_sett_frame.dispose();}catch(Exception ex){} try{color_frame.dispose();}catch(Exception ex){} } });

                game_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                game_frame.add(gaming_pane);
                game_frame.pack();
                game_frame.setLocationRelativeTo(null);
                game_frame.setVisible(true);
            }
        });
    }

    private void Re_Launch() { game_frame.dispose(); new Snake(); }
    private void credit_menu() { try {JOptionPane.showMessageDialog(null, "Creator Name: Pratik Patel\nCreated Date: 7/19/2020", "Credits", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(game_frame.getIconImage().getScaledInstance(100, 100, 0)));} catch(Exception ex) {} }

    private void set_snake_shape_btn_shape(JButton passed_btn)
    {
        passed_btn.setIcon(new Icon()
        {
			@Override public void paintIcon(Component c, Graphics g, int x, int y) 
            {
                Graphics2D temp_grapic = (Graphics2D) g.create();
                temp_grapic.setColor(Color.MAGENTA);
                int Hori_displace = 2;

                //0: square, 1: Triangle, 2: Circular
                if(snake_shape == 0) { temp_grapic.fillRect((passed_btn.getPreferredSize().height-getIconHeight())/2+Hori_displace , (passed_btn.getPreferredSize().height-getIconHeight())/2, getIconWidth(), getIconHeight()); }
                else if(snake_shape == 1) { temp_grapic.fillPolygon(new int[]{ (passed_btn.getPreferredSize().height-getIconHeight())/2+Hori_displace, (passed_btn.getPreferredSize().height-getIconHeight())/2+Hori_displace, (passed_btn.getPreferredSize().height-getIconHeight())/2+getIconWidth()+Hori_displace }, new int[]{ (passed_btn.getPreferredSize().height - getIconHeight())/2, (passed_btn.getPreferredSize().height - getIconHeight())/2 + getIconHeight(), (passed_btn.getPreferredSize().height - getIconHeight())/2 + getIconHeight()/2 }, 3); }
                else { temp_grapic.fillRect((passed_btn.getPreferredSize().height-getIconHeight())/2+Hori_displace , (passed_btn.getPreferredSize().height-getIconHeight())/2, getIconWidth()/2, getIconHeight()); temp_grapic.fillOval((passed_btn.getPreferredSize().height-getIconHeight())/2+Hori_displace, (passed_btn.getPreferredSize().height-getIconHeight())/2, getIconWidth(), getIconHeight()); }
			}

			@Override public int getIconWidth() { return  passed_btn.getPreferredSize().height-10; }
			@Override public int getIconHeight() { return  getIconWidth(); }
        });
    }

    private JFrame game_sett_frame;
    private void GameSettMenu()
    {
        try {game_sett_frame.dispose();} catch(Exception ex) {}

        game_sett_frame = new JFrame("Game Settings"); game_sett_frame.setIconImage(game_frame.getIconImage());
        game_sett_frame.setLayout(new GridBagLayout()); GridBagConstraints constr = new GridBagConstraints();

        JTextField time_delay_field = new JTextField(Integer.toString(time_delay) + " ms"); time_delay_field.setEditable(false); time_delay_field.setHorizontalAlignment(JTextField.CENTER); time_delay_field.setPreferredSize(new Dimension(time_delay_field.getPreferredSize().width+12, time_delay_field.getPreferredSize().height+5));
        JTextField obj_height_width_field = new JTextField(Integer.toString(obj_height_width)); obj_height_width_field.setEditable(false); obj_height_width_field.setHorizontalAlignment(JTextField.CENTER); obj_height_width_field.setPreferredSize(time_delay_field.getPreferredSize());
        JTextField initial_snake_length_field = new JTextField(Integer.toString(initial_snake_length)); initial_snake_length_field.setEditable(false); initial_snake_length_field.setHorizontalAlignment(JTextField.CENTER); initial_snake_length_field.setPreferredSize(time_delay_field.getPreferredSize());
        JTextField ScaleX_field = new JTextField(Integer.toString(ScaleX)); ScaleX_field.setEditable(false); ScaleX_field.setHorizontalAlignment(JTextField.CENTER); ScaleX_field.setPreferredSize(time_delay_field.getPreferredSize());
        JTextField ScaleY_field = new JTextField(Integer.toString(ScaleY)); ScaleY_field.setEditable(false); ScaleY_field.setHorizontalAlignment(JTextField.CENTER); ScaleY_field.setPreferredSize(time_delay_field.getPreferredSize());

        JSlider time_delay_slider = new JSlider(JSlider.HORIZONTAL, min_time_delay, max_time_delay, time_delay); time_delay_slider.setMajorTickSpacing(200); time_delay_slider.setMinorTickSpacing(10); time_delay_slider.setPaintTicks(true); time_delay_slider.setPaintLabels(true);
        JSlider obj_height_width_slider = new JSlider(JSlider.HORIZONTAL, min_obj_height_width, max_obj_height_width, obj_height_width); obj_height_width_slider.setMajorTickSpacing(10); obj_height_width_slider.setMinorTickSpacing(1); obj_height_width_slider.setPaintTicks(true); obj_height_width_slider.setPaintLabels(true);
        JSlider initial_snake_length_slider = new JSlider(JSlider.HORIZONTAL, min_initial_snake_length, max_initial_snake_length, initial_snake_length); initial_snake_length_slider.setMajorTickSpacing(10); initial_snake_length_slider.setMinorTickSpacing(1); initial_snake_length_slider.setPaintTicks(true); initial_snake_length_slider.setPaintLabels(true);
        JSlider ScaleX_slider = new JSlider(JSlider.HORIZONTAL, min_ScaleX, max_ScaleX, ScaleX); ScaleX_slider.setMajorTickSpacing(10); ScaleX_slider.setMinorTickSpacing(1); ScaleX_slider.setPaintTicks(true); ScaleX_slider.setPaintLabels(true);
        JSlider ScaleY_slider = new JSlider(JSlider.HORIZONTAL, min_ScaleY, max_ScaleY, ScaleY); ScaleY_slider.setMajorTickSpacing(10); ScaleY_slider.setMinorTickSpacing(1); ScaleY_slider.setPaintTicks(true); ScaleY_slider.setPaintLabels(true);

        JLabel time_delay_label = new JLabel("Screen Update Delay:"); time_delay_label.setHorizontalAlignment(JTextField.RIGHT); time_delay_label.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        JLabel obj_height_width_label = new JLabel("Object Height/Width:"); obj_height_width_label.setHorizontalAlignment(JTextField.RIGHT); obj_height_width_label.setFont(time_delay_label.getFont());
        JLabel initial_snake_length_label = new JLabel("Initial Snake Length:"); initial_snake_length_label.setHorizontalAlignment(JTextField.RIGHT); initial_snake_length_label.setFont(time_delay_label.getFont());
        JLabel scaleX_label = new JLabel("ScaleX:"); scaleX_label.setHorizontalAlignment(JTextField.RIGHT); scaleX_label.setFont(time_delay_label.getFont());
        JLabel scaleY_label = new JLabel("ScaleY:"); scaleY_label.setHorizontalAlignment(JTextField.RIGHT); scaleY_label.setFont(time_delay_label.getFont());
        JLabel is_dynamic_push_enabled_label = new JLabel("Dynamic Length:"); is_dynamic_push_enabled_label.setHorizontalAlignment(JTextField.RIGHT); is_dynamic_push_enabled_label.setFont(time_delay_label.getFont());
        JLabel snake_shape_label = new JLabel("Snake Head Shape: "); snake_shape_label.setHorizontalAlignment(JTextField.RIGHT); snake_shape_label.setFont(time_delay_label.getFont());
        JLabel edge_correction_label = new JLabel("Motion Correction: "); edge_correction_label.setHorizontalAlignment(JTextField.RIGHT); edge_correction_label.setFont(time_delay_label.getFont());
        JLabel smooth_motion_label = new JLabel("Smooth Motion: "); smooth_motion_label.setHorizontalAlignment(JTextField.RIGHT); smooth_motion_label.setFont(time_delay_label.getFont());

        scaleX_label.setPreferredSize(time_delay_label.getPreferredSize()); smooth_motion_label.setPreferredSize(scaleX_label.getPreferredSize()); scaleY_label.setPreferredSize(scaleX_label.getPreferredSize()); 
        initial_snake_length_label.setPreferredSize(scaleX_label.getPreferredSize()); obj_height_width_label.setPreferredSize(scaleX_label.getPreferredSize()); is_dynamic_push_enabled_label.setPreferredSize(scaleX_label.getPreferredSize());

        JButton is_dynamic_push_enabled_btn = new JButton((is_dynamic_push_enabled) ? ("ON") : ("OFF")); is_dynamic_push_enabled_btn.setPreferredSize(new JButton("OFF").getPreferredSize());
        is_dynamic_push_enabled_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { is_dynamic_push_enabled = !is_dynamic_push_enabled; is_dynamic_push_enabled_btn.setText((is_dynamic_push_enabled) ? ("ON") : ("OFF")); } });

        JButton snake_shape_btn = new JButton(); set_snake_shape_btn_shape(snake_shape_btn); snake_shape_btn.setPreferredSize(new Dimension(is_dynamic_push_enabled_btn.getPreferredSize().height+4, is_dynamic_push_enabled_btn.getPreferredSize().height));
        snake_shape_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { ++snake_shape; if(snake_shape == 3) { snake_shape = 0;} set_snake_shape_btn_shape(snake_shape_btn); } });

        JButton edge_correction_btn = new JButton((edge_correction) ? ("ON") : ("OFF")); edge_correction_btn.setPreferredSize(is_dynamic_push_enabled_btn.getPreferredSize());
        edge_correction_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { edge_correction = !edge_correction; edge_correction_btn.setText((edge_correction) ? ("ON") : ("OFF")); } });

        JButton smooth_motion_btn = new JButton((smooth_motion) ? ("ON") : ("OFF")); smooth_motion_btn.setPreferredSize(is_dynamic_push_enabled_btn.getPreferredSize());
        smooth_motion_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { smooth_motion_btn.setText(((smooth_motion_btn.getText() == "ON")?(false):(true)) ? ("ON") : ("OFF")); } });

        constr.insets = new Insets(5,5,5,5); game_sett_frame.add(time_delay_label, constr); constr.gridx = 1; game_sett_frame.add(time_delay_field, constr); constr.gridx = 2; constr.gridwidth = 3; game_sett_frame.add(time_delay_slider, constr);
        constr.gridwidth = 1; constr.gridx = 0; constr.gridy = 1; game_sett_frame.add(obj_height_width_label, constr); constr.gridx = 1; game_sett_frame.add(obj_height_width_field, constr); constr.gridx = 2; constr.gridwidth = 3; game_sett_frame.add(obj_height_width_slider, constr);
        constr.gridwidth = 1; constr.gridx = 0; constr.gridy = 2; game_sett_frame.add(initial_snake_length_label, constr); constr.gridx = 1; game_sett_frame.add(initial_snake_length_field, constr); constr.gridx = 2; constr.gridwidth = 3; game_sett_frame.add(initial_snake_length_slider, constr);
        constr.gridwidth = 1; constr.gridx = 0; constr.gridy = 3; game_sett_frame.add(scaleX_label, constr); constr.gridx = 1; game_sett_frame.add(ScaleX_field, constr); constr.gridx = 2; constr.gridwidth = 3; game_sett_frame.add(ScaleX_slider, constr);
        constr.gridwidth = 1; constr.gridx = 0; constr.gridy = 4; game_sett_frame.add(scaleY_label, constr); constr.gridx = 1; game_sett_frame.add(ScaleY_field, constr); constr.gridx = 2; constr.gridwidth = 3; game_sett_frame.add(ScaleY_slider, constr);
        constr.gridwidth = 1; constr.gridx = 0; constr.gridy = 5; game_sett_frame.add(is_dynamic_push_enabled_label, constr); constr.gridx = 1; game_sett_frame.add(is_dynamic_push_enabled_btn, constr); constr.gridwidth = 2; constr.gridx = 2; game_sett_frame.add(snake_shape_label, constr); constr.gridwidth = 1; constr.gridx = 4; game_sett_frame.add(snake_shape_btn, constr);
        constr.gridx = 0; constr.gridy = 6; game_sett_frame.add(smooth_motion_label, constr); constr.gridx = 1; game_sett_frame.add(smooth_motion_btn, constr); constr.gridwidth = 2; constr.gridx = 2; game_sett_frame.add(edge_correction_label, constr); constr.gridwidth = 1; constr.gridx = 4; game_sett_frame.add(edge_correction_btn, constr);

        time_delay_slider.addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent e) { gaming_pane.SETTimeDelay((int)((JSlider)e.getSource()).getValue()); time_delay_field.setText(Integer.toString(time_delay) + " ms"); } });
        obj_height_width_slider.addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent e) { obj_height_width_field.setText( Integer.toString((int)((JSlider)e.getSource()).getValue()) ); } });
        initial_snake_length_slider.addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent e) { gaming_pane.SETInitialSnakeLength((int)((JSlider)e.getSource()).getValue()); initial_snake_length_field.setText(Integer.toString(initial_snake_length)); } });
        ScaleX_slider.addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent e) { ScaleX_field.setText( Integer.toString((int)((JSlider)e.getSource()).getValue()) ); } });
        ScaleY_slider.addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent e) { ScaleY_field.setText( Integer.toString((int)((JSlider)e.getSource()).getValue()) ); } });

        JButton save_exit_btn = new JButton("Save & Exit"); constr.gridx = 2; constr.gridwidth = 2; constr.gridy = 7; game_sett_frame.add(save_exit_btn, constr);
        JButton reset_btn = new JButton("Reset"); constr.gridx = 4; constr.gridwidth = 1; game_sett_frame.add(reset_btn, constr);

        save_exit_btn.addActionListener(new ActionListener()
        {
            @Override public void actionPerformed(ActionEvent e) 
            {
                gaming_pane.SETObjHeiWid(Integer.parseInt(obj_height_width_field.getText()));
                gaming_pane.SETScaleX(Integer.parseInt(ScaleX_field.getText()));
                gaming_pane.SETScaleY(Integer.parseInt(ScaleY_field.getText()));
                smooth_motion = (smooth_motion_btn.getText() == "ON") ? (true) : (false);
                CreateSaveFile();
                game_sett_frame.dispose();
                Re_Launch();
            }
        });

        reset_btn.addActionListener(new ActionListener()
        {
            @Override public void actionPerformed(ActionEvent e) 
            {
                time_delay = 50;
                ScaleX = 40;
                ScaleY = 40;
                obj_height_width = 10;
                initial_snake_length = 1;
                is_dynamic_push_enabled = true;
                snake_shape = 0;
                smooth_motion = false;
                edge_correction = false;
                CreateSaveFile();
                game_sett_frame.dispose();
                Re_Launch();
            }
        });

        game_sett_frame.pack();
        game_sett_frame.setLocationRelativeTo(null);
        game_sett_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        game_sett_frame.setVisible(true);
    }

    private static JFrame color_frame;
    private void ColorSettMenu() 
    {
        try {color_frame.dispose();} catch(Exception ex) {}

        color_frame = new JFrame("Color Settings"); color_frame.setIconImage(game_frame.getIconImage());
        color_frame.setLayout(new GridBagLayout()); GridBagConstraints constr = new GridBagConstraints();

        constr.insets = new Insets(5,5,5,5);
        JSlider Red_slider = new JSlider(JSlider.VERTICAL, 0, 255, 255); Red_slider.setMajorTickSpacing(50); Red_slider.setMinorTickSpacing(10); Red_slider.setPaintTicks(true); Red_slider.setPaintLabels(true); Red_slider.setPreferredSize(new Dimension(Red_slider.getPreferredSize().width,Red_slider.getPreferredSize().height+80)); constr.gridheight = 9; color_frame.add(Red_slider, constr);
        JSlider Green_slider = new JSlider(JSlider.VERTICAL, 0, 255, 255); Green_slider.setMajorTickSpacing(50); Green_slider.setMinorTickSpacing(10); Green_slider.setPaintTicks(true); Green_slider.setPaintLabels(true); constr.gridx = 1; color_frame.add(Green_slider, constr); Green_slider.setPreferredSize(Red_slider.getPreferredSize());
        JSlider Blue_slider = new JSlider(JSlider.VERTICAL, 0, 255, 255); Blue_slider.setMajorTickSpacing(50); Blue_slider.setMinorTickSpacing(10); Blue_slider.setPaintTicks(true); Blue_slider.setPaintLabels(true); constr.gridx = 2; color_frame.add(Blue_slider, constr); Blue_slider.setPreferredSize(Red_slider.getPreferredSize());

        JTextField total_field = new JTextField(); total_field.setEditable(false); total_field.setBackground(Color.WHITE); constr.gridheight = 1; constr.gridx = 3; constr.gridy = 9; color_frame.add(total_field, constr);
        JTextField blue_field = new JTextField("255"); blue_field.setEnabled(false); blue_field.setHorizontalAlignment(JTextField.CENTER); blue_field.setBackground(Color.BLUE); constr.gridx = 2; color_frame.add(blue_field, constr);
        JTextField green_field = new JTextField("255"); green_field.setEditable(false); green_field.setHorizontalAlignment(JTextField.CENTER); green_field.setBackground(Color.GREEN); constr.gridx = 1; color_frame.add(green_field, constr);
        JTextField red_field = new JTextField("255"); red_field.setEditable(false); red_field.setHorizontalAlignment(JTextField.CENTER); red_field.setBackground(Color.RED); constr.gridx = 0; color_frame.add(red_field, constr);
        
        JButton BGColor_btn = new JButton("Background"); BGColor_btn.setBackground(BGColor);
        JButton FoodColor_btn = new JButton("Food"); FoodColor_btn.setBackground(FoodColor);
        JButton SnakeColor_btn = new JButton("Snake"); SnakeColor_btn.setBackground(SnakeColor);
        JButton SnakeCollisionColor_btn = new JButton("Collision"); SnakeCollisionColor_btn.setBackground(SnakeCollisionColor);
        JButton GameOverTextColor_btn = new JButton("Game Over"); GameOverTextColor_btn.setBackground(GameOverTextColor);
        JButton SnakeWonColor_btn = new JButton("Snake Won"); SnakeWonColor_btn.setBackground(SnakeWonColor);
        JButton SnakeWonTextColor_btn = new JButton("Game Won"); SnakeWonTextColor_btn.setBackground(SnakeWonTextColor);
        JButton save_exit_btn = new JButton("Save & Exit");
        JButton reset_btn = new JButton("Reset");

        save_exit_btn.setPreferredSize(BGColor_btn.getPreferredSize()); SnakeWonColor_btn.setPreferredSize(BGColor_btn.getPreferredSize()); SnakeWonTextColor_btn.setPreferredSize(BGColor_btn.getPreferredSize()); GameOverTextColor_btn.setPreferredSize(BGColor_btn.getPreferredSize()); FoodColor_btn.setPreferredSize(BGColor_btn.getPreferredSize()); SnakeColor_btn.setPreferredSize(BGColor_btn.getPreferredSize()); SnakeCollisionColor_btn.setPreferredSize(BGColor_btn.getPreferredSize()); reset_btn.setPreferredSize(BGColor_btn.getPreferredSize()); total_field.setPreferredSize(BGColor_btn.getPreferredSize());
        constr.gridx = 3; constr.gridy = 0; color_frame.add(BGColor_btn, constr); constr.gridy = 1; color_frame.add(FoodColor_btn, constr); constr.gridy = 2; color_frame.add(SnakeColor_btn, constr); constr.gridy = 3; color_frame.add(SnakeCollisionColor_btn, constr);  constr.gridy = 4;  color_frame.add(GameOverTextColor_btn, constr); constr.gridy = 5; color_frame.add(SnakeWonColor_btn, constr); constr.gridy = 6; color_frame.add(SnakeWonTextColor_btn, constr); constr.gridy = 7; color_frame.add(save_exit_btn, constr); constr.gridy = 8; color_frame.add(reset_btn, constr);

        red_field.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12)); green_field.setFont(red_field.getFont()); blue_field.setFont(green_field.getFont());

        BGColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETBackground(total_field.getBackground()); BGColor_btn.setBackground(BGColor); } });
        FoodColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETFoodColor(total_field.getBackground()); FoodColor_btn.setBackground(FoodColor); } });
        SnakeColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETSnakeColor(total_field.getBackground()); SnakeColor_btn.setBackground(SnakeColor); } });
        SnakeCollisionColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETSnakeCollisionColor(total_field.getBackground()); SnakeCollisionColor_btn.setBackground(SnakeCollisionColor); } });
        GameOverTextColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETGameOverTextColor(total_field.getBackground()); GameOverTextColor_btn.setBackground(GameOverTextColor); } });
        SnakeWonColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETSnakeWonColor(total_field.getBackground()); SnakeWonColor_btn.setBackground(SnakeWonColor); } });
        SnakeWonTextColor_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { gaming_pane.SETSnakeWonTextColor(total_field.getBackground()); SnakeWonTextColor_btn.setBackground(SnakeWonTextColor); } });
        save_exit_btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { color_frame.dispose(); CreateSaveFile(); } });
        reset_btn.addActionListener(new ActionListener() 
        { 
            @Override public void actionPerformed(ActionEvent e) 
            { 
                BGColor = Color.BLACK; FoodColor = Color.GREEN; SnakeColor = Color.WHITE; SnakeCollisionColor = Color.RED; GameOverTextColor = Color.ORANGE; SnakeWonColor = Color.YELLOW; SnakeWonTextColor = Color.BLUE; 
                gaming_pane.SETBackground(BGColor); BGColor_btn.setBackground(BGColor); 
                gaming_pane.SETFoodColor(FoodColor); FoodColor_btn.setBackground(FoodColor); 
                gaming_pane.SETSnakeColor(SnakeColor); SnakeColor_btn.setBackground(SnakeColor); 
                gaming_pane.SETSnakeCollisionColor(SnakeCollisionColor); SnakeCollisionColor_btn.setBackground(SnakeCollisionColor);
                gaming_pane.SETGameOverTextColor(GameOverTextColor); GameOverTextColor_btn.setBackground(GameOverTextColor);  
                gaming_pane.SETSnakeWonColor(SnakeWonColor); SnakeWonColor_btn.setBackground(SnakeWonColor); 
                gaming_pane.SETSnakeWonTextColor(SnakeWonTextColor); SnakeWonTextColor_btn.setBackground(SnakeWonTextColor); 
                CreateSaveFile(); 
            } 
        });

        Red_slider.addChangeListener(new ChangeListener() 
        {
            @Override public void stateChanged(ChangeEvent e) 
            {
                red_field.setBackground(new Color((int)((JSlider)e.getSource()).getValue(), 0, 0)); red_field.setText(Integer.toString((int)((JSlider)e.getSource()).getValue()));
                total_field.setBackground(new Color(red_field.getBackground().getRed(), total_field.getBackground().getGreen(), total_field.getBackground().getBlue()));
                if(Integer.parseInt(red_field.getText()) < 200) { red_field.setEnabled(false); } else { red_field.setEnabled(true); }
            }
        });

        Green_slider.addChangeListener(new ChangeListener() 
        {
            @Override public void stateChanged(ChangeEvent e) 
            {
                green_field.setBackground(new Color(0, (int)((JSlider)e.getSource()).getValue(), 0)); green_field.setText(Integer.toString((int)((JSlider)e.getSource()).getValue()));
                total_field.setBackground(new Color(total_field.getBackground().getRed(), green_field.getBackground().getGreen(), total_field.getBackground().getBlue()));
                if(Integer.parseInt(green_field.getText()) < 150) { green_field.setEnabled(false); } else { green_field.setEnabled(true); }
            }
        });

        Blue_slider.addChangeListener(new ChangeListener() 
        {
            @Override public void stateChanged(ChangeEvent e) 
            {
                blue_field.setBackground(new Color(0, 0, (int)((JSlider)e.getSource()).getValue())); blue_field.setText(Integer.toString((int)((JSlider)e.getSource()).getValue()));
                total_field.setBackground(new Color(total_field.getBackground().getRed(), total_field.getBackground().getGreen(), blue_field.getBackground().getBlue()));
            }
        });

        color_frame.pack();
        color_frame.setLocationRelativeTo(null);
        color_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        color_frame.setVisible(true);
    }


    //Snake Game setup starts here
    //TestPane variables**************************************************************************************
    private int time_delay;
    private int obj_height_width;
    private int initial_snake_length;
    private boolean is_dynamic_push_enabled;
    private int ScaleX;
    private int ScaleY;

    private Timer timer;
    private int x;
    private int y;
    private Dimension preferred_dim;
    private DimStack snake_path;
    private boolean game_on = true;
    private int move_direction = 0; //0:Right, 1:Down, 2:Left, 3:Up
    private int current_move_direction = 0;
    private boolean dynamic_push;
    private Dimension food_location;
    
    private Color FoodColor;
    private Color SnakeColor;
    private Color SnakeCollisionColor;
    private Color BGColor;
    private Color GameOverTextColor;
    private Color SnakeWonColor;
    private Color SnakeWonTextColor;

    private int snake_shape;
    private boolean smooth_motion;
    private boolean edge_correction;

    private final String File_name = "settings.txt";

    private final int min_time_delay = 0;
    private final int max_time_delay = 1000;
    private final int min_obj_height_width = 5;
    private final int max_obj_height_width = 40;
    private final int min_initial_snake_length = 1;
    private final int max_initial_snake_length = 50;
    private final int min_ScaleX = 1;
    private final int max_ScaleX = 100;
    private final int min_ScaleY = 1;
    private final int max_ScaleY = 100;

    public void RetriveSettings()
    {
        try
        {
            File in_file = new File(File_name);
            Scanner in = new Scanner(in_file);
            String temp = in.nextLine(); time_delay = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
            temp = in.nextLine(); obj_height_width = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
            temp = in.nextLine(); initial_snake_length = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
            temp = in.nextLine(); is_dynamic_push_enabled = (temp.substring(temp.indexOf(" ")+1).equals("T")) ? (true) : (false);
            temp = in.nextLine(); BGColor = new Color((Integer.parseInt(temp.substring(temp.indexOf(" ")+1))));
            temp = in.nextLine(); FoodColor = new Color(Integer.parseInt(temp.substring(temp.indexOf(" ")+1)));
            temp = in.nextLine(); SnakeColor = new Color(Integer.parseInt(temp.substring(temp.indexOf(" ")+1)));
            temp = in.nextLine(); SnakeCollisionColor = new Color(Integer.parseInt(temp.substring(temp.indexOf(" ")+1)));
            temp = in.nextLine(); GameOverTextColor = new Color(Integer.parseInt(temp.substring(temp.indexOf(" ")+1)));
            temp = in.nextLine(); SnakeWonColor = new Color(Integer.parseInt(temp.substring(temp.indexOf(" ")+1)));
            temp = in.nextLine(); SnakeWonTextColor = new Color(Integer.parseInt(temp.substring(temp.indexOf(" ")+1)));
            temp = in.nextLine(); ScaleX = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
            temp = in.nextLine(); ScaleY = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
            temp = in.nextLine(); snake_shape = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
            temp = in.nextLine(); edge_correction = (temp.substring(temp.indexOf(" ")+1).equals("T")) ? (true) : (false);
            temp = in.nextLine(); smooth_motion = (temp.substring(temp.indexOf(" ")+1).equals("T")) ? (true) : (false);
            in.close();

            if( !( time_delay >= min_time_delay && time_delay <= max_time_delay && 
                   obj_height_width >= min_obj_height_width && obj_height_width <= max_obj_height_width && 
                   initial_snake_length >= min_initial_snake_length && initial_snake_length <= max_initial_snake_length && 
                   ScaleX >= min_ScaleX && ScaleX <= max_ScaleX && 
                   ScaleY >= min_ScaleY && ScaleY <= max_ScaleY)) 
                { throw new Exception(); }
        }
        catch(Exception ex) {ResetDefaultSettings(true);}

        x = obj_height_width*-1; y = 0;
        preferred_dim = new Dimension(obj_height_width * ScaleX, obj_height_width * ScaleY);
        if(smooth_motion) { snake_path = new DimStack(initial_snake_length, obj_height_width); }
        else { snake_path = new DimStack(initial_snake_length, new Dimension(x,y), obj_height_width*-1); }
        food_location = preferred_dim;
    }

    public void CreateSaveFile()
    {
        try
        {
            File out_file = new File(File_name);
            FileWriter FW = new FileWriter(out_file);
            FW.write("time_delay " + time_delay + 
                     "\nobj_height_width " + obj_height_width +
                     "\ninitial_snake_length " + initial_snake_length +
                     "\nis_dynamic_push_enabled " + ((is_dynamic_push_enabled) ? ("T") : ("F")) +
                     "\nBGColor " + BGColor.getRGB() +
                     "\nFoodColor " + FoodColor.getRGB() +
                     "\nSnakeColor " + SnakeColor.getRGB() +
                     "\nSnakeCollisionColor " + SnakeCollisionColor.getRGB() +
                     "\nGameOverTextColor " + GameOverTextColor.getRGB() +
                     "\nSnakeWonColor " + SnakeWonColor.getRGB() +
                     "\nSnakeWonTextColor " + SnakeWonTextColor.getRGB() +
                     "\nScaleX " + ScaleX +
                     "\nScaleY " + ScaleY +
                     "\nsnake_shape " + snake_shape +
                     "\nedge_correction " + ((edge_correction) ? ("T") : ("F")) +
                     "\nsmooth_motion " + ((smooth_motion) ? ("T") : ("F")));
            FW.close();
        } catch(Exception ex) {}
    }

    public void ResetDefaultSettings(final boolean flag)
    {
        time_delay = 50;
        ScaleX = 40;
        ScaleY = 40;
        obj_height_width = 10;
        initial_snake_length = 1;
        is_dynamic_push_enabled = true;
        snake_shape = 0;
        smooth_motion = false;
        edge_correction = false;
        BGColor = Color.BLACK;
        FoodColor = Color.GREEN;
        SnakeColor = Color.WHITE;
        SnakeCollisionColor = Color.RED;
        GameOverTextColor = Color.ORANGE;
        SnakeWonTextColor = Color.BLUE;
        SnakeWonColor = Color.YELLOW;
        if(flag) {CreateSaveFile();}
    }
    //TestPane Things**************************************************************************************


    public class SnakeGamePane extends JPanel
    {
        private static final long serialVersionUID = 1L;
        private boolean Key_Pressed_up = false;
        private boolean Key_Pressed_down = false;
        private boolean Key_Pressed_right = false;
        private boolean Key_Pressed_left = false;
        private boolean update_snake_path_momentum = false;
        private boolean smooth_special_case_eat = false;

        public SnakeGamePane() 
        {
            super.setBackground(BGColor);
            super.setFocusable(true);

            if(smooth_motion) { Smooth_motion_gameplay(); }
            else { block_motion_gameplay(); }
        }

        private void block_motion_gameplay()
        {
            final int xBound = preferred_dim.width - obj_height_width;
            final int yBound = preferred_dim.height - obj_height_width;

            add_block_keys();

            timer = new Timer(time_delay, new ActionListener() 
            {
                @Override public void actionPerformed(ActionEvent e) 
                { 
                    if (move_direction == 0) { moveRight(); } 
                    else if (move_direction == 1) { moveDown(); } 
                    else if (move_direction == 2) { moveLeft(); } 
                    else { moveUp(); }

                    if(x == food_location.width && y == food_location.height) { dynamic_push = true; SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 )); }
                    else { dynamic_push = false; }
                    
                    if (x >= 0 && y >= 0 && x <= xBound && y <= yBound && snake_path.Push(x, y, dynamic_push && is_dynamic_push_enabled)) { repaint(); } 
                    else { game_on = false; repaint(); timer.stop(); }
                }
            });
        }

        private void add_block_keys()
        {
            super.addKeyListener(new KeyListener()
            {
                @Override public void keyPressed(KeyEvent e) 
                {
                    if(Key_Pressed_up || Key_Pressed_down || Key_Pressed_right || Key_Pressed_left) { return; }

                    if(e.getKeyCode() == KeyEvent.VK_RIGHT) 
                    { 
                        Key_Pressed_right = true; 
                        if(move_direction != 2 && move_direction != 0) { move_direction = 0; }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN) 
                    { 
                        Key_Pressed_down = true; 
                        if(move_direction != 3 && move_direction != 1) { move_direction = 1; }
                        
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT) 
                    { 
                        Key_Pressed_left = true; 
                        if(move_direction != 0 && move_direction != 2) { move_direction = 2; }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_UP) 
                    { 
                        Key_Pressed_up = true; 
                        if(move_direction != 1 && move_direction != 3) { move_direction = 3; }
                    }
                }
        
                @Override public void keyReleased(KeyEvent e) 
                { 
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT) { Key_Pressed_right = false; }
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN) { Key_Pressed_down = false; }
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT) { Key_Pressed_left = false; }
                    else if(e.getKeyCode() == KeyEvent.VK_UP) { Key_Pressed_up = false; } 
                }

                @Override public void keyTyped(KeyEvent e) {}
            });
        }

        private void Smooth_motion_gameplay()
        {
            final int xBound = preferred_dim.width - obj_height_width;
            final int yBound = preferred_dim.height - obj_height_width;

            add_smooth_keys();

            x += obj_height_width;
            timer = new Timer(time_delay, new ActionListener() 
            {
                @Override public void actionPerformed(ActionEvent e) 
                {
                    if(is_dynamic_push_enabled)
                    {
                        if(x%obj_height_width == 0 && y%obj_height_width == 0) 
                        { 
                            if(current_move_direction != move_direction)
                            {
                                current_move_direction = move_direction;     
                                if((current_move_direction == 0 && x == food_location.width-obj_height_width && y == food_location.height) || 
                                    (current_move_direction == 2 && x == food_location.width+obj_height_width && y == food_location.height) || 
                                    (current_move_direction == 1 && x == food_location.width && y == food_location.height-obj_height_width) || 
                                    (current_move_direction == 3 && x == food_location.width && y == food_location.height+obj_height_width)) 
                                    {smooth_special_case_eat = true;}
                            } 
                            update_snake_path_momentum = true; 
                        }

                        if(!smooth_special_case_eat)
                        {
                            if (current_move_direction == 0) { moveSmoothRight(); } 
                            else if (current_move_direction == 1) { moveSmoothDown(); } 
                            else if (current_move_direction == 2) { moveSmoothLeft(); } 
                            else { moveSmoothUp(); }
                        }
                        
                        if(x == food_location.width-obj_height_width && y == food_location.height && move_direction == current_move_direction && current_move_direction == 0) {x += obj_height_width; dynamic_push = true; SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));} 
                        else if(x == food_location.width+obj_height_width && y == food_location.height && move_direction == current_move_direction && current_move_direction == 2) {x -= obj_height_width; dynamic_push = true; SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));}
                        else if(x == food_location.width && y == food_location.height-obj_height_width && move_direction == current_move_direction && current_move_direction == 1) {y += obj_height_width; dynamic_push = true; SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));} 
                        else if(x == food_location.width && y == food_location.height+obj_height_width && move_direction == current_move_direction && current_move_direction == 3) {y -= obj_height_width; dynamic_push = true; SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));}
                        else { dynamic_push = false; }

                        if (x >= 0 && y >= 0 && x <= xBound && y <= yBound && snake_path.smoothPush(x, y, dynamic_push && true, current_move_direction, update_snake_path_momentum, smooth_special_case_eat)) { repaint(); update_snake_path_momentum = false; smooth_special_case_eat = false; } 
                        else { game_on = false; repaint(); timer.stop(); }
                    }

                    else
                    {
                        if(x%obj_height_width == 0 && y%obj_height_width == 0) 
                        { 
                            if(current_move_direction != move_direction) { current_move_direction = move_direction; } 
                            update_snake_path_momentum = true; 
                        }
                        
                        if (current_move_direction == 0) { moveSmoothRight(); } 
                        else if (current_move_direction == 1) { moveSmoothDown(); } 
                        else if (current_move_direction == 2) { moveSmoothLeft(); } 
                        else { moveSmoothUp(); }

                        if(current_move_direction == 0 && x == food_location.width+1-obj_height_width && y == food_location.height) { SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));} 
                        else if(current_move_direction == 2 && x == food_location.width-1+obj_height_width && y == food_location.height) { SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));}
                        else if(current_move_direction == 1 && x == food_location.width && y == food_location.height+1-obj_height_width) { SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));} 
                        else if(current_move_direction == 3 && x == food_location.width && y == food_location.height-1+obj_height_width) { SpawnFood(); game_score.setText(Integer.toString( Integer.parseInt(game_score.getText())+1 ));}

                        if (x >= 0 && y >= 0 && x <= xBound && y <= yBound && snake_path.smoothPush(x, y, false, current_move_direction, update_snake_path_momentum, false)) { repaint(); update_snake_path_momentum = false; } 
                        else { game_on = false; repaint(); timer.stop(); }
                    }
                }
            });
        }

        private void add_smooth_keys()
        {
            super.addKeyListener(new KeyListener()
            {
                @Override public void keyPressed(KeyEvent e) 
                {
                    if(Key_Pressed_up || Key_Pressed_down || Key_Pressed_right || Key_Pressed_left) { return; }

                    if(e.getKeyCode() == KeyEvent.VK_RIGHT) 
                    { 
                        Key_Pressed_right = true; 
                        if(current_move_direction != 2 && current_move_direction != 0) { move_direction = 0; }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN) 
                    { 
                        Key_Pressed_down = true; 
                        if(current_move_direction != 3 && current_move_direction != 1) { move_direction = 1; }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT) 
                    { 
                        Key_Pressed_left = true; 
                        if(current_move_direction != 0 && current_move_direction != 2) { move_direction = 2; }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_UP) 
                    { 
                        Key_Pressed_up = true; 
                        if(current_move_direction != 1 && current_move_direction != 3) { move_direction = 3; }
                    }
                }
        
                @Override public void keyReleased(KeyEvent e) 
                { 
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT) { Key_Pressed_right = false; }
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN) { Key_Pressed_down = false; }
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT) { Key_Pressed_left = false; }
                    else if(e.getKeyCode() == KeyEvent.VK_UP) { Key_Pressed_up = false; } 
                }

                @Override public void keyTyped(KeyEvent e) {}
            });
        }

        private void Reset() 
        {
            timer.stop();
            preferred_dim = new Dimension(obj_height_width * ScaleX, obj_height_width * ScaleY);
            food_location = preferred_dim;
            dynamic_push = false;
            Key_Pressed_up = false;
            Key_Pressed_down = false;
            Key_Pressed_right = false;
            Key_Pressed_left = false;
            game_on = true;
            move_direction = 0;
            x = obj_height_width*-1;
            y = 0;
            if(smooth_motion) { snake_path = new DimStack(initial_snake_length, obj_height_width); x+=obj_height_width; update_snake_path_momentum = false; smooth_special_case_eat = false; current_move_direction = 0;}
            else { snake_path = new DimStack(initial_snake_length, new Dimension(x,y), obj_height_width*-1); }
            game_score.setText("0");
            repaint();
        }

        private void moveRight() { x += obj_height_width; }
        private void moveLeft() { x -= obj_height_width; }
        private void moveUp() { y -= obj_height_width; }
        private void moveDown() { y += obj_height_width; }

        private void moveSmoothRight() { ++x; }
        private void moveSmoothLeft() { --x; }
        private void moveSmoothUp() { --y; }
        private void moveSmoothDown() { ++y; }

        private void SETTimeDelay(final int delay) { time_delay = delay; timer.setDelay(time_delay); }
        private void SETObjHeiWid(final int length) { obj_height_width = length; }
        private void SETInitialSnakeLength(final int length) { initial_snake_length = length; Reset(); start_game.setEnabled(false); stop_game.setEnabled(true); restart_game.setEnabled(true); }
        private void SETBackground(final Color user_color) { BGColor = user_color; super.setBackground(BGColor); }
        private void SETFoodColor(final Color user_color) { FoodColor = user_color; repaint(); }
        private void SETSnakeColor(final Color user_color) { SnakeColor = user_color; repaint(); }
        private void SETSnakeCollisionColor(final Color user_color) { SnakeCollisionColor = user_color; repaint(); }
        private void SETGameOverTextColor(final Color user_color) { GameOverTextColor = user_color; repaint(); }
        private void SETSnakeWonColor(final Color user_color) { SnakeWonColor = user_color; repaint(); }
        private void SETSnakeWonTextColor(final Color user_color) { SnakeWonTextColor = user_color; repaint(); }
        private void SETScaleX(final int user_scale) { ScaleX = user_scale; }
        private void SETScaleY(final int user_scale) { ScaleY = user_scale; }
        private void SpawnFood() { food_location = snake_path.advance_spawn_food(preferred_dim, food_location); }

        private void Start() { timer.start(); }
        private void Stop() { timer.stop(); }

        @Override public Dimension getPreferredSize() { return preferred_dim; }

        private int[] TriangleXPts = new int[3];
        private int[] TriangleYPts = new int[3];
        private int[] CircularPts = new int[4];

        @Override protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(FoodColor); g2d.fillRect(food_location.width, food_location.height, obj_height_width, obj_height_width);

            if(game_on) { g2d.setColor(SnakeColor); } 
            else 
            {
                stop_game.setEnabled(false);

                if(snake_path.getSizeOnScreen() < (preferred_dim.width/obj_height_width)*(preferred_dim.height/obj_height_width) && food_location.width != -1*obj_height_width && food_location.height != -1*obj_height_width)
                {
                    g2d.setColor(SnakeCollisionColor);  DrawSnake(g2d);
                    g.setColor(GameOverTextColor); g.setFont(new Font(Font.DIALOG, Font.BOLD, 18)); g.drawString("GAME OVER", getPreferredSize().width/2 - 55, getPreferredSize().height/2);
                    return;
                }
                else
                {
                    g2d.setColor(SnakeWonColor);  DrawSnake(g2d);
                    g.setColor(SnakeWonTextColor); g.setFont(new Font(Font.DIALOG, Font.BOLD, 18)); g.drawString("GAME WON", getPreferredSize().width/2 - 45, getPreferredSize().height/2);
                    return;
                }
            }

            DrawSnake(g2d);
        }

        private void DrawSnake(Graphics g2d)
        {
            final Dimension[] snake_location = snake_path.getDimArr(edge_correction && smooth_motion); 
            if(snake_shape == 1) { TriangleShapeHead(); g2d.fillPolygon(TriangleXPts, TriangleYPts, 3); for(int i = 1; i < snake_location.length; ++i) { g2d.fillRect(snake_location[i].width, snake_location[i].height, obj_height_width, obj_height_width); }}
            else if(snake_shape == 2) { CircleShapeHead(); g2d.fillRect(CircularPts[0], CircularPts[1], CircularPts[2], CircularPts[3]); g2d.fillOval(snake_location[0].width, snake_location[0].height, obj_height_width, obj_height_width); for(int i = 1; i < snake_location.length; ++i) { g2d.fillRect(snake_location[i].width, snake_location[i].height, obj_height_width, obj_height_width); } }
            else { for (Dimension D : snake_location) { g2d.fillRect(D.width, D.height, obj_height_width, obj_height_width); } }
            g2d.dispose();
        }

        private void CircleShapeHead()
        {
            if(game_on)
            {
                if(smooth_motion)
                {
                    //0:Right, 1:Down, 2:Left, 3:Up
                    if(current_move_direction == 0) { CircularPts[0] = x; CircularPts[1] = y; CircularPts[2] = obj_height_width/2; CircularPts[3] = obj_height_width; }
                    else if(current_move_direction == 1) { CircularPts[0] = x; CircularPts[1] = y; CircularPts[2] = obj_height_width; CircularPts[3] = obj_height_width/2; }
                    else if(current_move_direction == 2) { CircularPts[0] = x+obj_height_width/2; CircularPts[1] = y; CircularPts[2] = obj_height_width/2; CircularPts[3] = obj_height_width; }
                    else { CircularPts[0] = x; CircularPts[1] = y+obj_height_width/2; CircularPts[2] = obj_height_width; CircularPts[3] = obj_height_width/2; }
                }
                else
                {
                    //0:Right, 1:Down, 2:Left, 3:Up
                    if(move_direction == 0) { CircularPts[0] = x; CircularPts[1] = y; CircularPts[2] = obj_height_width/2; CircularPts[3] = obj_height_width; }
                    else if(move_direction == 1) { CircularPts[0] = x; CircularPts[1] = y; CircularPts[2] = obj_height_width; CircularPts[3] = obj_height_width/2; }
                    else if(move_direction == 2) { CircularPts[0] = x+obj_height_width/2; CircularPts[1] = y; CircularPts[2] = obj_height_width/2; CircularPts[3] = obj_height_width; }
                    else { CircularPts[0] = x; CircularPts[1] = y+obj_height_width/2; CircularPts[2] = obj_height_width; CircularPts[3] = obj_height_width/2; }
                }
            }
        }

        private void TriangleShapeHead()
        {
            if(game_on)
            {
                if(smooth_motion)
                {
                    if(current_move_direction == 0)  //0:Right, 1:Down, 2:Left, 3:Up
                    {
                        TriangleXPts[0] = x; TriangleXPts[1] = x+obj_height_width; TriangleXPts[2] = x;
                        TriangleYPts[0] = y; TriangleYPts[1] = y+(int)(obj_height_width/2); TriangleYPts[2] = y+obj_height_width;
                    }
                    else if(current_move_direction == 1)
                    {
                        TriangleXPts[0] = x; TriangleXPts[1] = x+obj_height_width; TriangleXPts[2] = x+(int)(obj_height_width/2);
                        TriangleYPts[0] = y; TriangleYPts[1] = y; TriangleYPts[2] = y+obj_height_width;
                    }
                    else if(current_move_direction == 2)
                    {
                        TriangleXPts[0] = x+obj_height_width; TriangleXPts[1] = TriangleXPts[0]; TriangleXPts[2] = x;
                        TriangleYPts[0] = y; TriangleYPts[1] = y+obj_height_width; TriangleYPts[2] = y+(int)(obj_height_width/2);
                    }
                    else
                    {
                        TriangleXPts[0] = x; TriangleXPts[1] = x+obj_height_width; TriangleXPts[2] = x+(int)(obj_height_width/2);
                        TriangleYPts[0] = y+obj_height_width; TriangleYPts[1] = TriangleYPts[0]; TriangleYPts[2] = y;
                    }
                }
                else
                {
                    if(move_direction == 0)  //0:Right, 1:Down, 2:Left, 3:Up
                    {
                        TriangleXPts[0] = x; TriangleXPts[1] = x+obj_height_width; TriangleXPts[2] = x;
                        TriangleYPts[0] = y; TriangleYPts[1] = y+(int)(obj_height_width/2); TriangleYPts[2] = y+obj_height_width;
                    }
                    else if(move_direction == 1)
                    {
                        TriangleXPts[0] = x; TriangleXPts[1] = x+obj_height_width; TriangleXPts[2] = x+(int)(obj_height_width/2);
                        TriangleYPts[0] = y; TriangleYPts[1] = y; TriangleYPts[2] = y+obj_height_width;
                    }
                    else if(move_direction == 2)
                    {
                        TriangleXPts[0] = x+obj_height_width; TriangleXPts[1] = TriangleXPts[0]; TriangleXPts[2] = x;
                        TriangleYPts[0] = y; TriangleYPts[1] = y+obj_height_width; TriangleYPts[2] = y+(int)(obj_height_width/2);
                    }
                    else
                    {
                        TriangleXPts[0] = x; TriangleXPts[1] = x+obj_height_width; TriangleXPts[2] = x+(int)(obj_height_width/2);
                        TriangleYPts[0] = y+obj_height_width; TriangleYPts[1] = TriangleYPts[0]; TriangleYPts[2] = y;
                    }
                }
            }
        }
    }
}


/*
if (game_on) { g2d.setColor(SnakeColor); } 
else { g2d.setColor(SnakeCollisionColor); stop_game.setEnabled(false); }

if(custom_snake_shape)
{
    if(game_on)
    {
        if(smooth_motion)
        {
            if(current_move_direction == 0)  //0:Right, 1:Down, 2:Left, 3:Up
            {
                XPts[0] = x; XPts[1] = x+obj_height_width; XPts[2] = x;
                YPts[0] = y; YPts[1] = y+(int)(obj_height_width/2); YPts[2] = y+obj_height_width;
            }
            else if(current_move_direction == 1)
            {
                XPts[0] = x; XPts[1] = x+obj_height_width; XPts[2] = x+(int)(obj_height_width/2);
                YPts[0] = y; YPts[1] = y; YPts[2] = y+obj_height_width;
            }
            else if(current_move_direction == 2)
            {
                XPts[0] = x+obj_height_width; XPts[1] = XPts[0]; XPts[2] = x;
                YPts[0] = y; YPts[1] = y+obj_height_width; YPts[2] = y+(int)(obj_height_width/2);
            }
            else
            {
                XPts[0] = x; XPts[1] = x+obj_height_width; XPts[2] = x+(int)(obj_height_width/2);
                YPts[0] = y+obj_height_width; YPts[1] = YPts[0]; YPts[2] = y;
            }
        }
        else
        {
            if(move_direction == 0)  //0:Right, 1:Down, 2:Left, 3:Up
            {
                XPts[0] = x; XPts[1] = x+obj_height_width; XPts[2] = x;
                YPts[0] = y; YPts[1] = y+(int)(obj_height_width/2); YPts[2] = y+obj_height_width;
            }
            else if(move_direction == 1)
            {
                XPts[0] = x; XPts[1] = x+obj_height_width; XPts[2] = x+(int)(obj_height_width/2);
                YPts[0] = y; YPts[1] = y; YPts[2] = y+obj_height_width;
            }
            else if(move_direction == 2)
            {
                XPts[0] = x+obj_height_width; XPts[1] = XPts[0]; XPts[2] = x;
                YPts[0] = y; YPts[1] = y+obj_height_width; YPts[2] = y+(int)(obj_height_width/2);
            }
            else
            {
                XPts[0] = x; XPts[1] = x+obj_height_width; XPts[2] = x+(int)(obj_height_width/2);
                YPts[0] = y+obj_height_width; YPts[1] = YPts[0]; YPts[2] = y;
            }
        }
    }

    g2d.fillPolygon(XPts, YPts, 3);
    for(int i = 1; i < snake_path.getSize(); ++i) { g2d.fillRect(snake_location[i].width, snake_location[i].height, obj_height_width, obj_height_width); }
}
else { for (Dimension D : snake_location) { g2d.fillRect(D.width, D.height, obj_height_width, obj_height_width); } }

if(!game_on) {g.setColor(GameOverTextColor); g.setFont(new Font(Font.DIALOG, Font.BOLD, 18)); g.drawString("GAME OVER", getPreferredSize().width/2 - 45, getPreferredSize().height/2 - 10);}
*/
