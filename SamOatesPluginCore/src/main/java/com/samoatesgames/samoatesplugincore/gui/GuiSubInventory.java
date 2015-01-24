package com.samoatesgames.samoatesplugincore.gui;

public abstract class GuiSubInventory extends GuiInventory {

    final private GuiInventory ownerMenu;

    public GuiSubInventory(GuiInventory ownerMenu, String title, int rowCount) {
        super(ownerMenu.getPlugin());
        this.createInventory(title, rowCount);
        this.ownerMenu = ownerMenu;
    }

    /**
     *
     * @return
     */
    @Override
    public String getOwnerTitle() {
        return this.ownerMenu.getOwnerTitle();
    }
}
