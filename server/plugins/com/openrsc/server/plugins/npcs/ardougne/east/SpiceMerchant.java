package com.openrsc.server.plugins.npcs.ardougne.east;

import com.openrsc.server.model.Shop;
import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.rsc.ActionSender;
import com.openrsc.server.plugins.ShopInterface;
import com.openrsc.server.plugins.listeners.action.TalkToNpcListener;
import com.openrsc.server.plugins.listeners.executive.TalkToNpcExecutiveListener;

import java.time.Instant;

import static com.openrsc.server.plugins.Functions.*;

public class SpiceMerchant implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

	private final Shop shop = new Shop(false, 15000, 100, 70, 2, new Item(707, 1));

	@Override
	public void onTalkToNpc(Player p, Npc n) {
		if (p.getCache().hasKey("spiceStolen") && Instant.now().getEpochSecond() < p.getCache().getLong("spiceStolen") + 1200) {
			npcTalk(p, n, "Do you really think I'm going to buy something",
				"That you have just stolen from me",
				"guards guards");

			Npc attacker = getNearestNpc(p, 324, 5); // Hero first
			if (attacker == null)
				attacker = getNearestNpc(p, 323, 5); // Paladin second
			if (attacker == null)
				attacker = getNearestNpc(p, 322, 5); // Knight third
			if (attacker == null)
				attacker = getNearestNpc(p, 321, 5); // Guard fourth

			if (attacker != null)
				attacker.setChasing(p);

		} else {
			npcTalk(p, n, "Get your exotic spices here",
				"rare very valuable spices here");
			int menu = showMenu(p, n, "Lets have a look then", "No thank you I'm not interested");
			if (menu == 0) {
				p.setAccessingShop(shop);
				ActionSender.showShop(p, shop);
			}
		}
	}

	// WHEN STEALING AND CAUGHT BY A MERCHANT ("Hey thats mine");
	// Delay player busy (3000); after stealing and Npc shout out to you.

	@Override
	public boolean blockTalkToNpc(Player p, Npc n) {
		return n.getID() == 329;
	}

	@Override
	public Shop[] getShops() {
		return new Shop[]{shop};
	}

	@Override
	public boolean isMembers() {
		return true;
	}
}
