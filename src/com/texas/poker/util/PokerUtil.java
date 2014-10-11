package com.texas.poker.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.texas.poker.R;
import com.texas.poker.entity.ClientPlayer;
import com.texas.poker.entity.Poker;

public class PokerUtil {
	
    public static final int FLUSH = 9;
    
    public static final int KING_KONG = 8;
  
    public static final int GOURD = 7;

    public static final int ROYAL_FLUSH = 6;

    public static final int STRAIGHT = 5;

    public static final int THREE = 4;

    public static final int TWO_PAIR = 3;

    public static final int PAIR = 2;

    public static final int HIGH_CARD = 1;
    
    public static int[] pokersImg = {
        R.drawable.diamond1, R.drawable.diamond2, R.drawable.diamond3, R.drawable.diamond4,
        R.drawable.diamond5, R.drawable.diamond6, R.drawable.diamond7, R.drawable.diamond8,
        R.drawable.diamond9, R.drawable.diamond10, R.drawable.diamond11, R.drawable.diamond12,
        R.drawable.diamond13, R.drawable.club1, R.drawable.club2, R.drawable.club3,
        R.drawable.club4, R.drawable.club5, R.drawable.club6, R.drawable.club7,
        R.drawable.club8, R.drawable.club9, R.drawable.club10, R.drawable.club11,
        R.drawable.club12, R.drawable.club13, R.drawable.heart1, R.drawable.heart2,
        R.drawable.heart3, R.drawable.heart4, R.drawable.heart5, R.drawable.heart6,
        R.drawable.heart7, R.drawable.heart8, R.drawable.heart9, R.drawable.heart10,
        R.drawable.heart11, R.drawable.heart12, R.drawable.heart13, R.drawable.spade1,
        R.drawable.spade2, R.drawable.spade3, R.drawable.spade4, R.drawable.spade5,
        R.drawable.spade6, R.drawable.spade7, R.drawable.spade8, R.drawable.spade9,
        R.drawable.spade10, R.drawable.spade11, R.drawable.spade12, R.drawable.spade13
};
	
	public static ArrayList<Poker> getPokers(int personsCount){
	    ArrayList<Poker> pokers = new ArrayList(); 
		int pokerLenght = personsCount*2+5;
		Random rd = new Random();
		int num ;
		for(int i = pokerLenght - 1;i>=0;i--){
			num = rd.nextInt(51);
			Poker p = new Poker(num);
			if(!pokers.contains(p)){
				pokers.add(p);
			}else{
				i++;
			}
		}
		return pokers;
	}
	
	public static HashMap<String,ArrayList<ClientPlayer>> getWinner(ArrayList<ClientPlayer> playerList,ArrayList<Poker> poker){
		HashMap<String,ArrayList<ClientPlayer>> players = new HashMap<String, ArrayList<ClientPlayer>>();
		ArrayList pokersType = new ArrayList();
		ArrayList<Poker> pokersBack = new ArrayList<Poker>();
		for(int i = 0;i<playerList.size();i++){
			ArrayList<Poker> box = new ArrayList<Poker>();
			Poker p1 = new Poker(poker.get(i*2));
			Poker p2 = new Poker(poker.get(i*2+1));
			Poker p3 = new Poker(poker.get(poker.size()-1));
			Poker p4 = new Poker(poker.get(poker.size()-2));
			Poker p5 = new Poker(poker.get(poker.size()-3));
			Poker p6 = new Poker(poker.get(poker.size()-4));
			Poker p7 = new Poker(poker.get(poker.size()-5));
			box.add(p1);
			box.add(p2);
			box.add(p3);
			box.add(p4);
			box.add(p5);
			box.add(p6);
			box.add(p7);
			playerList.get(i).getInfo().setCardType(getPokerType(box, pokersBack));
			ArrayList<Poker> temp = new ArrayList<Poker>();
			temp.addAll(pokersBack);
			playerList.get(i).getInfo().setPokerBack(temp);
			if(!pokersType.contains(playerList.get(i).getInfo().getCardType())){
				pokersType.add(playerList.get(i).getInfo().getCardType());
			}
		}
		for(int i = 0;i<pokersType.size();i++){
			int max = Integer.parseInt(pokersType.get(i).toString());
			for(int j = i;j<pokersType.size();j++){
				if(Integer.parseInt(pokersType.get(j).toString())>max){
					int temp = Integer.parseInt(pokersType.get(j).toString());
					pokersType.set(j, max);
					max = temp;
				}
			}
			pokersType.set(i, max);
		}
		
		for(int i = 0;i<playerList.size();i++){
			for(int j = 0;j<pokersType.size();j++){
				if(playerList.get(i).getInfo().getCardType() == Integer.parseInt(pokersType.get(j).toString())){
					if(players.get(String.valueOf(j)) == null){
						ArrayList<ClientPlayer> temp = new ArrayList<ClientPlayer>();
						temp.add(playerList.get(i));
						players.put(String.valueOf(j), temp);
					}else{
						players.get(String.valueOf(j)).add(playerList.get(i));
						players.put(String.valueOf(j),players.get(String.valueOf(j)));
					}
				}
			}
			
		}
		
		return players;
	}


	public static int getPokerType(ArrayList<Poker> pokers,ArrayList<Poker> pokersBack){
		insertSort(pokers);
//		pokersBack = new ArrayList<Poker>();
		ArrayList<Poker> pokersBackUp = new ArrayList<Poker>();
		if(checkoutFivePokerColorSame(pokers,pokersBack)){
		    if(chekcoutPokerSucceedingNumbers(pokersBack,pokersBackUp)){
		        pokersBack = pokersBackUp;
		        return FLUSH*1000+countPokerNumber(pokersBack);
		    }else{
		        return ROYAL_FLUSH*1000+countPokerNumber(pokersBack);
		    }
		}else if(chekcoutPokerSucceedingNumbers(pokers,pokersBack)){
		    return STRAIGHT*1000+countPokerNumber(pokersBack);
		}else if(chekcoutFourPokerNumber(pokers,pokersBack)){
		    return KING_KONG*1000+countPokerNumber(pokersBack);
		}else if(checkoutThreePokerNumber(pokers,pokersBack)){
		    pokers.removeAll(pokersBack);
		    if(checkoutTwoPokerNumber(pokers,pokersBackUp)){
		        pokersBack.addAll(pokersBackUp);
		        return GOURD*1000+countPokerNumber(pokersBack);
		    }else{
		        return THREE*1000+countPokerNumber(pokersBack);
		    }
		}else if(checkoutTwoPokerNumber(pokers,pokersBack)){
		    pokers.removeAll(pokersBack);
		    if(checkoutTwoPokerNumber(pokers,pokersBackUp)){
		        pokersBack.addAll(pokersBackUp);
		        return TWO_PAIR*1000+countPokerNumber(pokersBack);
		    }else{
		        return PAIR*1000+countPokerNumber(pokersBack);
		    }
		}else{
			
		    return HIGH_CARD*1000+heighCardCount(pokers);
		}
	}
	
    public static int countPokerNumber(ArrayList<Poker> pokers){
    	int sum = 0;
    	for(int i = pokers.size()-1;i>=0;i--){
    		sum +=pokers.get(i).getNumber();
    	}
    	return sum;
    }
    
    public static int heighCardCount(ArrayList<Poker> pokers){
    	int max = 0;
    	if(pokers.get(0).getSize() == 0){
			pokers.get(0).setSize(14);
		}
    	max = pokers.get(0).getSize()*7+pokers.get(0).getColor();
    	for(int i = 0;i<pokers.size();i++){
    		if(pokers.get(i).getSize() == 0){
    			pokers.get(i).setSize(14);
    		}
    		if(pokers.get(i).getSize()*7+pokers.get(i).getColor()>max){
    			max = pokers.get(i).getSize()*7+pokers.get(i).getColor();
    		}
    		if(pokers.get(i).getSize() == 14){
    			pokers.get(i).setSize(0);
    		}
    	}
    	return max;
    }
	
	public static boolean checkoutFivePokerColorSame(ArrayList<Poker> pokers,ArrayList<Poker> pokersBack){
		int temp = 0;
		Poker box = new Poker();
		int currentIndex = -1;
		for(int i = pokers.size() - 1;i>=4;i--){
			box = pokers.get(i);
			currentIndex = i;
			temp++;
			for(int j = i-1;j>=0;j--){
				 if(pokers.get(i).getColor() == pokers.get(j).getColor()){
					 temp++;
				 }
			}
			if(temp<5){
			    temp = 0;
			}else{
			    break;
			}
		}
		if(temp >= 5){
			for(int n = 0;n<currentIndex&&temp>=0;n++){
                if(pokers.get(n).getColor() == box.getColor()){
                    Poker pLast = new Poker(pokers.get(n));
                    pokersBack.add(pLast);
                    temp--;
                }
			}
            Poker p = new Poker(box);
            pokersBack.add(p);
			return true;
		}
		return false;
	}
	
	public static boolean chekcoutPokerSucceedingNumbers(ArrayList<Poker> pokers,ArrayList<Poker> pokersBack){
		int temp = 0;
    		for(int i = pokers.size()-1;i>=4;i--){
        			for(int j = i;j>=1;j--){
        				if(pokers.get(j).getSize() == pokers.get(j-1).getSize()+1||
        				        (pokers.get(j).getSize() == 1&&pokers.get(j).getSize() == pokers.get(j-1).getSize()-12)){
        					temp++;
        					Poker p = new Poker(pokers.get(j));
        					pokersBack.add(p);
        					if(temp == 4){
                                Poker pLast = new Poker(pokers.get(j-1));
                                pokersBack.add(pLast);
        						return true;
        					}
        				}else if(pokers.get(j).getSize() != pokers.get(j-1).getSize()){
        					break;
        				}
        			}
        			if(temp == 4){
        				return true;
        			}else{
        			    temp = 0;
        			    pokersBack.clear();
        			}
        		}
//    		1 2 3 4 5������
    		if(pokers.get(pokers.size()-1).getSize() == 0&&pokers.get(0).getSize() == 1){
    		    pokersBack.add(pokers.get(pokers.size()-1));
    		    temp++;
    		    for(int i = 0;i<pokers.size()-1;i++){
    		        if(pokers.get(i).getSize() == pokers.get(i+1).getSize()-1){
                        temp++;
                        Poker p = new Poker(pokers.get(i));
                        pokersBack.add(p);
                        if(temp == 4){
                            Poker pLast = new Poker(pokers.get(i+1));
                            pokersBack.add(pLast);
                            return true;
                        }
                    }else if(pokers.get(i).getSize() != pokers.get(i+1).getSize()){
                        break;
                    }
                }
                if(temp == 4){
                    return true;
                }else{
                    temp = 0;
                    pokersBack.clear();
                }
    		}
		return false;
	}
	
//	�ж��Ƿ�����4����������ͬ
	public static boolean chekcoutFourPokerNumber(ArrayList<Poker> pokers,ArrayList<Poker> pokersBack){
	    
	    return checkNumberSame(pokers,4,pokersBack);
	}
	
//	�ж��Ƿ�����3����������ͬ
	public static boolean checkoutThreePokerNumber(ArrayList<Poker> pokers,ArrayList<Poker> pokersBack){

	    return checkNumberSame(pokers,3,pokersBack);
	}
	
//	�ж��Ƿ�����2����������ͬ
	public static boolean checkoutTwoPokerNumber(ArrayList<Poker> pokers,ArrayList<Poker> pokersBack){

	    return checkNumberSame(pokers,2,pokersBack);
	}
	
	public static ArrayList<Poker> insertSort(ArrayList<Poker> pokers){//���������㷨
//	             ������1������14,��������
	    for(int i = pokers.size()-1;i>=0;i--){
	        if(pokers.get(i).getSize() == 0){
	            pokers.get(i).setSize(14);
	        }
	    }
	    
        for(int i = 1;i<pokers.size();i++){
                for(int j=i;j>0;j--){
                    if (pokers.get(j).getSize()<pokers.get(j-1).getSize()){
                                Poker temp = new Poker(pokers.get(j-1));
                                pokers.get(j-1).setPoker(pokers.get(j));
                                pokers.get(j).setPoker(temp);
                    }else break;
                }
        }
        
//      �ָ����е�14
        for(int i = pokers.size()-1;i>=0;i--){
            if(pokers.get(i).getSize() == 14){
                pokers.get(i).setSize(0);
            }
        }
        return pokers;
	}
	
	public static boolean checkNumberSame(ArrayList<Poker> pokers,int size,ArrayList<Poker> pokersBack){
	    int temp = 0;
        Poker box = new Poker();
        int currentIndex = -1;
        for(int i = pokers.size() - 1;i>=(size-1);i--){
            box = pokers.get(i);
            currentIndex = i;
            for(int j = i-1;j>=0;j--){
                 if(pokers.get(i).getSize() == pokers.get(j).getSize()){
                     temp++;
                     if(temp == size - 1){
                         break;
                     }
                 }
            }
            if(temp == size - 1){
                break;
            }else{
                temp = 0;
            }
        }
        if(temp == size - 1){
            Poker p = new Poker(box);
            pokersBack.add(p);
            temp--;
            for(int n = currentIndex-1;n>=0&&temp>=0;n--){
                if(pokers.get(n).getSize() == box.getSize()){
                    Poker pLast = new Poker(pokers.get(n));
                    pokersBack.add(pLast);
                    temp--;
                }
            }
            return true;
        }
        return false;
	}

//	��������
	public static boolean CountChips(ArrayList chips,ArrayList callBack){
	    if(!chips.isEmpty()){
	        Collections.sort(chips);
	        if(!callBack.isEmpty()){
                callBack.clear();
	        }
	        int box = 0;
	        for(int i = 0;i<chips.size();i++){
	            if(Integer.parseInt(chips.get(i).toString())!=0){
    	            callBack.add(Integer.parseInt(chips.get(i).toString())*(chips.size()-i));
    	            box = Integer.parseInt(chips.get(i).toString());
    	            for(int j = 0;j<chips.size();j++){
    	                chips.set(j, Integer.parseInt(chips.get(j).toString()) - 
    	                		box); 
    	            }
	            }
	            
	        }
	    }
        return false;
	}
	
	public static int getWinMoney(String winnerId,ArrayList<ClientPlayer> playerList){
		int winner = 0;
		for(int i = 0;i<playerList.size();i++){
			if(playerList.get(i).getInfo().getId().equals(winnerId)){
				winner = playerList.get(i).getInfo().getAroundSumChip();
			}
		}
		int sum = 0;
		for(int i = 0;i<playerList.size();i++){
			if(winner>=playerList.get(i).getInfo().getAroundSumChip()){
				sum += playerList.get(i).getInfo().getAroundSumChip();
				playerList.get(i).getInfo().setAroundSumChip(0);
			}else{
				sum += (playerList.get(i).getInfo().getAroundSumChip() - winner);
				playerList.get(i).getInfo().setAroundSumChip(playerList.get(i).getInfo().getAroundSumChip() - winner);
			}
		}
		return sum;
	}
	
	public static String getCardTypeString(int cardType){
	        switch(cardType/1000){
	            case HIGH_CARD:
	                return "高牌";
	            case PAIR:
                    return "一对";
	            case TWO_PAIR:
                    return "两对";
	            case THREE:
                    return "三条";
	            case STRAIGHT:
                    return "顺子";
	            case ROYAL_FLUSH:
                    return "同花";
	            case GOURD:
                    return "葫芦";
	            case KING_KONG:
                    return "四条";
	            case FLUSH:
                    return "同花顺";
	        }
	        return "高牌";
	}
	
	public static int getCardType(int cardType){
        switch(cardType/1000){
            case HIGH_CARD:
                return 0;
            case PAIR:
                return 1;
            case TWO_PAIR:
                return 2;
            case THREE:
                return 3;
            case STRAIGHT:
                return 4;
            case ROYAL_FLUSH:
                return 5;
            case GOURD:
                return 6;
            case KING_KONG:
                return 7;
            case FLUSH:
                return 8;
        }
        return 9;
}

}
