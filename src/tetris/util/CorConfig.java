package tetris.util;

import java.awt.Color;

/**
 * Configurações visuais (modo colorido / monocromático e tema claro/escuro).
 * Singleton simples usado pela UI para decidir cores.
 */
public final class CorConfig {
    private static final CorConfig INSTANCE = new CorConfig();

    private boolean modoColorido = true;
    private boolean modoMonocromatico = false;
    private Color corMonocromatica = Color.LIGHT_GRAY;
    private boolean temaEscuro = true;

    private CorConfig(){}

    public static CorConfig get() { return INSTANCE; }

    public boolean isModoColorido() { return modoColorido; }
    public void setModoColorido(boolean on) { modoColorido = on; if (on) modoMonocromatico = false; }

    public boolean isModoMonocromatico() { return modoMonocromatico; }
    public void setModoMonocromatico(boolean on) { modoMonocromatico = on; if (on) modoColorido = false; }

    public Color getCorMonocromatica() { return corMonocromatica; }
    public void setCorMonocromatica(Color c) { if (c != null) corMonocromatica = c; }

    public boolean isTemaEscuro() { return temaEscuro; }
    public void setTemaEscuro(boolean escuro) { temaEscuro = escuro; }
}
