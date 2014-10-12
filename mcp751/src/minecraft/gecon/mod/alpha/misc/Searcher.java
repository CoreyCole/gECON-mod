package gecon.mod.alpha.misc;

import gecon.mod.alpha.BankItem;
import gecon.mod.alpha.BankItemDuo;

import java.util.ArrayList;

public class Searcher {
	public static ArrayList<BankItem> recomb(ArrayList<BankItem> searchList, String search) {
		ArrayList<BankItem> list = new ArrayList<BankItem>();
		for(BankItem x: searchList) {
			if(x.items.get(0).getDisplayName().toLowerCase().indexOf(search) >= 0) {
				list.add(x);
			}
		}
		return list;
	}
	
	public static ArrayList<BankItemDuo> recomb2(ArrayList<BankItemDuo> searchList, String search){
		ArrayList<BankItemDuo> list = new ArrayList<BankItemDuo>();
		
		for(BankItemDuo x: searchList) {
			if(x.items.get(0).getDisplayName().toLowerCase().indexOf(search) >= 0) {
				list.add(x);
			}
		}
		
		return list;
	}
}
