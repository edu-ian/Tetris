package tetris.ui;

import tetris.domain.Partida;
import tetris.engine.GameEngine;
import tetris.util.CorConfig;
import tetris.util.Dificuldade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Janela principal: monta layout, menus, controles e inicializa engine.
 * Usa Key Bindings (InputMap/ActionMap) para controlar o jogo.
 */
public class TelaPrincipal extends JFrame implements GameEngine.Listener {

    private final Partida partida;
    private final GameEngine engine;
    private final CorConfig corConfig;

    private final GamePanel gamePanel;
    private final ScorePanel scorePanel;

    public TelaPrincipal() {
        super("Tetris - Trabalho Acadêmico");

        // configurações iniciais
        this.corConfig = CorConfig.get();
        this.partida = new Partida(true, null); // modo colorido por padrão
        this.engine = new GameEngine(partida, Dificuldade.MEDIO, corConfig, this);

        // UI
        this.gamePanel = new GamePanel(engine, corConfig);
        this.scorePanel = new ScorePanel();

        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.EAST);

        setupMenu();
        setupKeyBindings();

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setupMenu() {
        JMenuBar mb = new JMenuBar();

        JMenu mJogo = new JMenu("Jogo");
        JMenuItem miIniciar = new JMenuItem(new AbstractAction("Iniciar") {
            @Override public void actionPerformed(ActionEvent e) { engine.iniciar(); }
        });
        JMenuItem miPausar = new JMenuItem(new AbstractAction("Pausar/Continuar") {
            @Override public void actionPerformed(ActionEvent e) { engine.alternarPausa(); gamePanel.repaint(); }
        });
        JMenuItem miReiniciar = new JMenuItem(new AbstractAction("Reiniciar") {
            @Override public void actionPerformed(ActionEvent e) { engine.reiniciar(); gamePanel.repaint(); scorePanel.atualizar(partida.getPontuacao()); }
        });
        mJogo.add(miIniciar); mJogo.add(miPausar); mJogo.add(miReiniciar);

        JMenu mDif = new JMenu("Dificuldade");
        for (Dificuldade d : Dificuldade.values()) {
            mDif.add(new JMenuItem(new AbstractAction(d.name()) {
                @Override public void actionPerformed(ActionEvent e) { engine.setDificuldade(d); }
            }));
        }

        JMenu mVis = new JMenu("Visual");
        JMenuItem miColor = new JMenuItem(new AbstractAction("Modo Colorido") {
            @Override public void actionPerformed(ActionEvent e) { corConfig.setModoColorido(true); gamePanel.repaint(); }
        });
        JMenuItem miMono = new JMenuItem(new AbstractAction("Modo Monocromático") {
            @Override public void actionPerformed(ActionEvent e) {
                Color chosen = JColorChooser.showDialog(TelaPrincipal.this, "Escolha cor", corConfig.getCorMonocromatica());
                if (chosen != null) corConfig.setCorMonocromatica(chosen);
                corConfig.setModoMonocromatico(true);
                gamePanel.repaint();
            }
        });
        JMenuItem miTema = new JMenuItem(new AbstractAction("Alternar Tema Claro/Escuro") {
            @Override public void actionPerformed(ActionEvent e) {
                corConfig.setTemaEscuro(!corConfig.isTemaEscuro()); gamePanel.repaint();
            }
        });
        mVis.add(miColor); mVis.add(miMono); mVis.add(miTema);

        mb.add(mJogo); mb.add(mDif); mb.add(mVis);
        setJMenuBar(mb);
    }

    private void setupKeyBindings() {
        // Key Bindings no painel para foco correto
        InputMap im = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = gamePanel.getActionMap();

        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("UP"), "rotate");
        im.put(KeyStroke.getKeyStroke("DOWN"), "soft");
        im.put(KeyStroke.getKeyStroke("SPACE"), "hard");
        im.put(KeyStroke.getKeyStroke("Q"), "hold");
        im.put(KeyStroke.getKeyStroke("P"), "pause");

        am.put("left", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { engine.moverEsquerda(); gamePanel.repaint(); }});
        am.put("right", new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) { engine.moverDireita(); gamePanel.repaint(); }});
        am.put("rotate", new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) { engine.rotacionar(); gamePanel.repaint(); }});
        am.put("soft", new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) { engine.softDrop(); scorePanel.atualizar(partida.getPontuacao()); gamePanel.repaint(); }});
        am.put("hard", new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) { engine.hardDrop(); scorePanel.atualizar(partida.getPontuacao()); gamePanel.repaint(); }});
        am.put("hold", new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) { partida.hold(); gamePanel.repaint(); scorePanel.atualizar(partida.getPontuacao()); }});
        am.put("pause", new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) { engine.alternarPausa(); gamePanel.repaint(); }});
    }

    public void iniciar() { setVisible(true); engine.iniciar(); }

    // Listener do engine
    @Override
    public void onUpdate() { scorePanel.atualizar(partida.getPontuacao()); gamePanel.repaint(); }

    @Override
    public void onGameOver() {
        scorePanel.atualizar(partida.getPontuacao());
        gamePanel.repaint();
        JOptionPane.showMessageDialog(this, "Game Over!\nPontuação: " + partida.getPontuacao().getPontos());
    }
}
