import java.io.*;
import java.util.Calendar;
import java.util.*;
import javax.swing.*;
import java.awt.*;
abstract class Ship{
	// общие для всех поля
	int[] size = new int[3]; 	// размер size[0]-длина, size[1]-ширина, size[2]-высота (м) 
	double massa; 				// масса или водоизмещение (тонны)
	double fuel; 				// емкость топливного бака(ов) (тонны)
	double outlayFuel; 			// средний расход топлива при движении (литр/км)
	int speed; 					// максимальная скорость 
	int[][] course; 			// курс - долгота и широта портов по курсу следования
	int[] location = new int[2]; // местонахождение - долгота и широта
	Calendar dateStart = Calendar.getInstance(); // время отправления
	Calendar dateEnd = Calendar.getInstance();	 // время прибытия в конечный пункт
	boolean repair = false;
	// конструктор
	Ship(int[] size,double massa,double fuel,double outlayFuel, int speed,int[][] course){
		this.massa = massa;
		this.fuel = fuel;
		this.outlayFuel = outlayFuel;
		this.speed = speed;
		for(int i=0; i<3; i++){
			this.size[i] = size[i];
		}
		this.course = new int[course.length][2];
		for(int i = 0; i<course.length; i++){
			for(int j=0; j<2; j++) {
				this.course[i][j] = course[i][j];
			}
		}
		for(int i = 0; i<2; i++) {
			this.location[i] = course[0][i];
		}
	}
	// методы
	void repair() { // ремонт
		if (repair == false) repair = true;
		else System.out.println("Текущий ремонт проведен"); //??? запланировать на дату и проверять
	}
	void fuel(double f) { // заправка топливом - возвращает значение топлива для помещения в поле fuel
		fuel += f; 
	}
	double outlayFuel(int[] start, int[] end) { // расход топлива
		return outlayFuel*distance(start, end)/100;
	}
	void changeCourse(int n, int[] crs) { // изменение курса, n - № порта по курсу, crs - координаты нового пункта
		course[n][0] = crs[0];
		course[n][1] = crs[1];
	}
	double distance(int[] start, int[] end) { // пройденное расстояние
		// формула расчета дистанции {1° = 0.017453292519943 rad}
		double pi180 = (Math.PI)/180;
		double lat1 = start[0] * pi180;	// широта 
		double lng1 = start[1] * pi180;	// долгота
		double lat2 = end[0] * pi180; 	// широта
		double lng2 = end[1] * pi180; 	// долгота
 
		double r = 6372.797; 			// радиус Земли в км
		double dlat = (lat2 - lat1)/2; 	//дельта широты
		double dlng = (lng2 - lng1)/2; 	//дельта долготы
	
		double a = Math.sin(dlat) * Math.sin(dlat) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlng) * Math.sin(dlng);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return r * c;
	}
	void dateEnd(int[] start, int[] end, int year, int mounth, int day, int hour, int minutes) { // время прибытия
		dateStart.set(year, mounth, day, hour, minutes);
		dateEnd.set(year, mounth, day, hour, minutes);
		double dlnway = distance(start, end);
		int dayway = (int)(dlnway/speed);
		dateEnd.add(dateEnd.DATE, dayway);
	}
	void location(int[] l) { // местонахождение
		location[0] = l[0];
		location[1] = l[1];
	}
	abstract void show(); 	// вывод информации о корабле 
	abstract void showfield(JTextField[] txtfield); 	// вывод информации о корабле
}
abstract class Military extends Ship{
	int ammunition; 		// боекомплект
	int restammunition; 	// осаток боекомплекта
	Military(int[] s,double m,double f,double oF, int sp,int[][] c, int ammunition){
		super(s,m,f,oF,sp,c);
		this.ammunition = ammunition;
		this.restammunition = this.ammunition; 
	}
	abstract void shooting(int n); 			// стрельба -> расход боекомплекта
	abstract void addAmmunition(int n); 	// пополнение боекомплекта
}
abstract class Truck extends Ship{
	double trum; 		// емкость трюма
	double resttrum; 	// остаток емкости трюма
	String gruz = "|"; 	// наименование груза
	double massGruz=0; 	// масса груза
	Truck(int[] s,double m,double f,double oF, int sp,int[][] c,double trum) {
		super(s, m, f, oF, sp, c);
		this.trum = trum;
		this.resttrum = this.trum; 
	}
	abstract void loading(double n, String t);  	// погрузка
	abstract void unloading(double n, String t); 	// разрузка
}
abstract class Civil extends Ship{
	int passenger; 		// кол-во пассажирских мест
	int restpassenger; 	// кол-во пассажиров
	Civil(int[] s,double m,double f,double oF, int sp,int[][] c,int passenger) {
		super(s,m,f,oF, sp,c);
		this.passenger = passenger;
		restpassenger = 0;
	}
	void embarkation(int n) { 	// посадка пассажиров
		if (restplays() >= n) {	
			restpassenger += n;
			System.out.println("На борт принято: "+n+" пассажир(ов)");
		} else System.out.println("Каюты заполнены,\nостаток пустых кают: "+(passenger-restpassenger));
	}
	void disembarkation(int n) { // высадка пассажиров
		if (restpassenger >= n) {
			restpassenger -= n;
			System.out.println("Высажено на берег: "+n+" пассажир(a,ов)");
		} else System.out.println("Каюты пусты,\nостаток пассажиров:"+restpassenger);
	}
	int restplays() { // кол-во свободных мест
		return passenger - restpassenger;  
	}
}
interface Fishing {
	abstract void fishing(String[] v_fish); // ловля рыбы -> вид рыбы (заказ тон)-> 
									  		// заполнение трюма
}
interface Air {
	abstract void take_off(int a); 	// взлет
	abstract void landing(int a); 	// посадка на авиановец
}
// ланер
class Liner extends Civil {
	int num_flow;		// кол-во этажей
	int num_helth;		// кол-во зон отдыха
	int[][] f_plays;	// каюты по этажам
	Liner(int[] s,double m,double f,double oF, int sp,int[][] c,int p, int nf, int nh) {
		super(s,m,f,oF,sp,c,p);
		num_flow = nf;
		num_helth = nh;
		f_plays = new int[num_flow][(int)(p/num_flow)];
	}
	void embarkation(int n) { // посадка пассажиров
		int num_p=0; // кол-во принятых на борт пассажиров
		if ((restplays() >= n)||((restplays()<n)&(restplays()>0))) {
			for (int i=0; i<f_plays.length; i++){
				for (int j=0; j<f_plays[0].length; j++) {
					if (f_plays[i][j]==0) {
						f_plays[i][j]=1;
						num_p++;
					}
					if (num_p == n) break;
				}
				if (num_p == n) break;
			}
			restpassenger += num_p;
			System.out.println("Размещено: "+num_p+" пассажир(ов) \nостаток " +
					"свободных мест: "+restplays());
		} else System.out.println("Каюты заполнены,\nостаток пустых кают:"+(passenger-restpassenger)); 
	}	
	void disembarkation(int n) { // высадка пассажиров
		int num_p = 0; // кол-во высаженых на берег пассажиров
		if ((restpassenger > 0)&(num_p < n)) {
			for (int i=0; i<f_plays.length; i++) {
				for (int j=0; j<f_plays[0].length; j++) {
					if (f_plays[i][j]==1) {
						f_plays[i][j]=0;
						num_p++;
						restpassenger--;
					}
					if (num_p == n) break;
				}
				if (num_p == n) break;
			}
			System.out.println("Высажено на берег: "+num_p+" пассажир(ов)\nостаток" +
					" пассажиров: "+restpassenger);
		} else System.out.println("На судне нет пассажиров:\n кол-во пассажиров:"+restpassenger+
				" свободных мест: "+restplays());
	}
	void show() {
		String txt = "\"ЛАЙНЕР\"\nТехнические характеристики:\n";
		txt += "Водоизмещение: "+ massa+"\n";
		txt += "Длина: "+ size[0]+"\n";
		txt += "Ширина: "+ size[1]+"\n";
		txt += "Максимальная скорость: "+ speed+" узлов\n";
		txt += "Пассажирских палуб: "+ num_flow+"\n";
		txt += "Кают на этаже: "+ (int)(passenger/num_flow)+"\n";
		txt += "Всего пассажирских мест: "+ passenger+"\n";
		txt += "из них свободных мест: "+ (passenger-restpassenger)+"\n";
		txt += "Зон отдыха: "+ num_helth+"\n";
		txt += "Дата отправления: "+ dateStart.getTime()+"\n";
		txt += "Дата предположительного прибытия: "+ dateEnd.getTime()+"\n";
		txt += "На борту всего: "+ restpassenger+" пассажиров";
		JOptionPane.showMessageDialog(null, txt);
	}
	void showfield(JTextField[] txtfield) {
		int[] s = new int[2];
		int[] e = new int[2];
		s[0] = course[0][0];
		s[1] = course[0][1];
		e[0] = course[1][0];
		e[1] = course[1][1];
		txtfield[0].setText(""+massa+" тонн.");
		txtfield[1].setText(""+size[0]+" метров.");
		txtfield[2].setText(""+size[1]+" метров.");
		txtfield[3].setText(""+speed+" узлов.");
		txtfield[4].setText(""+num_flow);
		txtfield[5].setText(""+(int)(passenger/num_flow));
		txtfield[6].setText(""+passenger);
		txtfield[7].setText(""+(passenger-restpassenger));
		txtfield[8].setText(""+num_helth);
		txtfield[9].setText(""+dateStart.getTime());
		txtfield[10].setText(""+dateEnd.getTime());
		txtfield[11].setText("Долгота/Широта - "+course[0][0]+"/"+course[0][1]+" : "+course[1][0]+"/"+course[1][1]);
		txtfield[12].setText(""+distance(s, e));
		txtfield[13].setText(""+outlayFuel(s, e));
		txtfield[14].setText(""+restpassenger+" пассажиров");
	}
}
// яхта
class Yacht extends Civil {
	// вид яхты: парусная, моторная, паруно-моторная
	String[] vid = {"парусная","моторная","паруно-моторная"};
	String v_yacht;
	Yacht(int[] s,double m,double f,double oF, int sp,int[][] c,int p, int vid) {
		super(s,m,f,oF,sp,c,p);
		v_yacht = this.vid[vid];
	}
	void show() {
		
	}
	void showfield(JTextField[] txtfield) {
		
	}
}
// танкер
class Tanker extends Truck {
	int q_tank = 1; 				// кол-во танков
	double v_tank = trum/q_tank; 	// емкость танка
	Tanker(int[] s,double m,double f,double oF, int sp,int[][] c,double trum, int q_tank) {
		super(s, m, f, oF, sp, c, trum);
		this.q_tank = q_tank;
		v_tank = trum/this.q_tank;
	}
	void loading(double n, String t) { // погрузка
		if (resttrum - n > 0) {
			massGruz +=n;
			resttrum -= n;
			gruz = t+"|";
			System.out.println("Погрузка завершена. Груз:"+t+" "+massGruz+" m3");
		} else System.out.println("Все танки заполнены,\nостаток емкости трюма:"+resttrum+
				" m3 \nили "+resttrum/v_tank+" танка");
	}
	void unloading(double n, String t) { // разрузка
		if (massGruz - n >= 0) {
			massGruz -=n;
			resttrum +=n;
			if (resttrum == trum){
				gruz = gruz.replaceAll(t,"");
			} 
			System.out.println("Разгрузка завершена.\nОстаток груза:"+t+" "+massGruz+" m3\n"+
					"остаток места "+resttrum+" m3 \nили "+resttrum/v_tank+" танка");
		}
	}
	void show() {
		String txt = "\"ТАНКЕР\"\nТехнические характеристики:\n";
		txt += "Водоизмещение: "+ massa+"\n";
		txt += "Длина: "+ size[0]+"\n";
		txt += "Ширина: "+ size[1]+"\n";
		txt += "Максимальная скорость: "+ speed+" узлов\n";
		txt += "Количество танков: "+ q_tank+"\n";
		txt += "Емкость танка: "+ v_tank+"\n";
		txt += "Емкость трюма: "+ trum+"\n";
		txt += "Остаток емкости трюма: "+ resttrum+"\n";
		txt += "Наименование груза: "+ gruz+"\n";
		txt += "Масса груза: "+ massGruz+"\n";
		txt += "Дата отправления: "+ dateStart.getTime()+"\n";
		txt += "Дата предположительного прибытия: "+ dateEnd.getTime()+"\n";
		JOptionPane.showMessageDialog(null, txt);
	}
	void showfield(JTextField[] txtfield) {
		int[] s = new int[2];
		int[] e = new int[2];
		s[0] = course[0][0];
		s[1] = course[0][1];
		e[0] = course[1][0];
		e[1] = course[1][1];
		txtfield[0].setText(""+massa+" тонн.");
		txtfield[1].setText(""+size[0]+" метров.");
		txtfield[2].setText(""+size[1]+" метров.");
		txtfield[3].setText(""+speed+" узлов.");
		txtfield[4].setText(""+q_tank);
		txtfield[5].setText(""+v_tank);
		txtfield[6].setText(""+trum);
		txtfield[7].setText(""+resttrum);
		txtfield[8].setText(""+gruz);
		txtfield[9].setText(""+massGruz);
		txtfield[10].setText(""+dateStart.getTime());
		txtfield[11].setText(""+dateEnd.getTime());
		txtfield[12].setText("Долгота/Широта - "+course[0][0]+"/"+course[0][1]+" : "+course[1][0]+"/"+course[1][1]);
		txtfield[13].setText(""+distance(s, e));
		txtfield[14].setText(""+outlayFuel(s, e));
	}
}
// баржа
class Barge extends Truck {
	Barge(int[] s,double m,double f,double oF, int sp,int[][] c,double trum) {
		super(s, m, f, oF, sp, c, trum);
	}
	void loading(double n, String t) { // погрузка
		if (resttrum - n > 0) {
			massGruz +=n;
			resttrum -= n;
			gruz += t+"|";
			System.out.println("Груз "+t+" масой "+n+" тонн загружен");
		} else System.out.println("Баржа заполнена,\nостаток места для засыпки:"+resttrum);
	}
	void unloading(double n, String t) { // разрузка
		if (massGruz >= n) {
			trum -= n;
			massGruz -=n;
			resttrum +=n;
			gruz = gruz.replaceAll(t, "");
			System.out.println("Разружено "+t+" массой "+n+" тонн");
			System.out.println("Остаток груза:"+t+" "+massGruz+" тонн\n"+
			"остаток места "+resttrum+" тонн");
		}
	}
	void show_trum() {
		System.out.println("Груз на перевозку: "+gruz+" массой - "+massGruz+" тонн");
		String txt;
		txt = "Груз на перевозку: "+gruz+" массой - "+massGruz+" тонн";
		JOptionPane.showMessageDialog(null, txt);
	}
	void show() {
		String txt = "\"БАРЖА\"\nТехнические характеристики:\n";
		txt += "Водоизмещение: "+ massa+"\n";
		txt += "Длина: "+ size[0]+"\n";
		txt += "Ширина: "+ size[1]+"\n";
		txt += "Максимальная скорость: "+ speed+" узлов\n";
		txt += "Емкость трюма: "+ trum+"\n";
		txt += "Остаток емкости трюма: "+ resttrum+"\n";
		txt += "Наименование груза: "+ gruz+"\n";
		txt += "Масса груза: "+ massGruz+"\n";
		txt += "Дата отправления: "+ dateStart.getTime()+"\n";
		txt += "Дата предположительного прибытия: "+ dateEnd.getTime()+"\n";
		JOptionPane.showMessageDialog(null, txt);
	}
	void showfield(JTextField[] txtfield) {
		int[] s = new int[2];
		int[] e = new int[2];
		s[0] = course[0][0];
		s[1] = course[0][1];
		e[0] = course[1][0];
		e[1] = course[1][1];
		txtfield[0].setText(""+massa+" тонн.");
		txtfield[1].setText(""+size[0]+" метров.");
		txtfield[2].setText(""+size[1]+" метров.");
		txtfield[3].setText(""+speed+" узлов.");
		txtfield[4].setText(""+trum);
		txtfield[5].setText(""+resttrum);
		txtfield[6].setText(""+gruz);
		txtfield[7].setText(""+massGruz);
		txtfield[8].setText(""+dateStart.getTime());
		txtfield[9].setText(""+dateEnd.getTime());
		txtfield[10].setText("Долгота/Широта - "+course[0][0]+"/"+course[0][1]+" : "+course[1][0]+"/"+course[1][1]);
		txtfield[11].setText(""+distance(s, e));
		txtfield[12].setText(""+outlayFuel(s, e));
	}
}
// эсминец
class Esminec extends Military {
	Esminec(int[] s,double m,double f,double oF, int sp,int[][] c, int am){
		super(s,m,f,oF,sp,c,am);
	}
	void shooting(int n) { // стрельба -> расход боекомплекта
		if (restammunition - n < 0) {
			System.out.println("Стрельба невозможна,\nнедостаточно боеприпасов"+restammunition);
		} else {
			restammunition -= n;
			System.out.println("Стрельба осуществлена.\nИзрасходовано-"+n+" единиц\n" +
					"остаток - "+restammunition+" единиц");
		}
	}
	void addAmmunition(int n) { // пополнение боекомплекта
		if (restammunition + n <= ammunition) {
			restammunition += n;
			System.out.println("Боекомплект пополнен на "+n+" единиц.\nВсего: "+restammunition+" единиц");
		} else System.out.println("Недостаток емкости боекомплекта:"+restammunition);
	}
	void show() {
		
	}
	void showfield(JTextField[] txtfield){
		
	}	
}
// авианосец
class Aircraft extends Military implements Air {
	int number_airplane; // кол-во самолетов
	int airplane = 0; // фактическое кол-во самолетов
	Aircraft(int[] s,double m,double f,double oF, int sp,int[][] c, int am, int plane){
		super(s,m,f,oF,sp,c,am);
		number_airplane = plane;
		airplane = plane;
	}
	void addAmmunition(int n) { // пополнение боекомплекта
		if (restammunition + n <= ammunition) {
			restammunition += n;
			System.out.println("Боекомплект пополнен на "+n+" единиц.\nВсего: "+restammunition+" единиц");
		} else System.out.println("Недостаток емкости боекомплекта:"+restammunition);
		if (airplane < number_airplane) airplane += number_airplane - airplane; 
	}
	void shooting(int n) { // стрельба -> расход боекомплекта
		if (restammunition - n < 0) {
			System.out.println("Стрельба зенитных орудий невозможна, \nнедостаточно боеприпасов"+restammunition);
		} else {
			restammunition -= n;
			System.out.println("Стрельба осуществлена. Израсходовано-"+n+" единиц\n" +
					"остаток - "+restammunition);
		}
		if (airplane > 0) {
			take_off(1);
			System.out.println("Самолет для охраны поднят в воздух");
		} else System.out.println("Все самолеты в воздухе: "+airplane+" - охрана невозможна");
	}
	public void take_off(int a) { // взлет
		if (airplane >= a) {
			airplane -= a;
			System.out.println("Подняты в воздух "+a+" самолетов"+"\nостаток на авианосце: "+airplane);
		} else System.out.println("Все самолеты подняты в воздух, кол-во на судне: "+airplane);
	}
	public void landing(int a) { // посадка
		if (a <= number_airplane - airplane) {
			airplane += a;
			System.out.println("Посадку осуществили "+a+" самолетов"+"\nостаток на авианосце: "+airplane);
		} else {
			System.out.println("На посадку приняли "+(number_airplane - airplane));
			System.out.println("На посадку не приняли "+(a-(number_airplane - airplane))+
					"\nвсе посадочные места заняты,");
			airplane = number_airplane;
			System.out.println("самолетов на борту "+airplane);
		}
	}
	void show() {
		
	}
	void showfield(JTextField[] txtfield){
		
	}
}
// рыболовецкое судно
class FishShip extends Truck implements Fishing {
	String[][] vid_fish; // 0 - вид рыбы, 1 - заказ тонн, 2 - фактически выловлено
	FishShip(int[] s,double m,double f,double oF, int sp,int[][] c,double trum, String[][] v_fish) {
		super(s, m, f, oF, sp, c, trum);
		this.vid_fish = new String[v_fish.length][v_fish[0].length];
		for (int i=0; i<vid_fish.length; i++){
			for (int j=0; j<vid_fish[0].length; j++) {
				this.vid_fish[i][j] = v_fish[i][j];
			} 
		}
	}
	public void fishing(String[] v_fish) { // рыбалка 0 -номер в vid_fish, 1 - тонны
		  if (Integer.parseInt(vid_fish[(int)Integer.parseInt(v_fish[0])][1])-Integer.parseInt(vid_fish[(int)Integer.parseInt(v_fish[0])][2]) 
				>= Integer.parseInt(v_fish[1])) {
			loading(Double.parseDouble(v_fish[1]),vid_fish[(int)Integer.parseInt(v_fish[0])][0]);
			int temp = Integer.parseInt(vid_fish[(int)Integer.parseInt(v_fish[0])][2])
					+Integer.parseInt(v_fish[1]);
			vid_fish[(int)Integer.parseInt(v_fish[0])][2] = ""+temp;
			for (int i=0; i<vid_fish.length; i++){
				for (int j=0; j<vid_fish[0].length; j++) {
					System.out.print(vid_fish[i][j]+" |");
				}
				System.out.println();
			}
		} else System.out.println("Вид рыбы:"+vid_fish[(int)Integer.parseInt(v_fish[0])][0]+" уже выловлен");
	}
	void loading(double n, String t) { // погрузка
		if (resttrum - n > 0) {
			massGruz +=n;
			resttrum -= n;
			gruz += t+"|";
			System.out.println("Груз "+t+" массой "+n+" загружен");
		} else System.out.println("Ледник заполнен,\nостаток места для улова:"+resttrum);
	}
	void unloading(double n, String t) { // разрузка 
		for(int i=0; i<vid_fish.length; i++){
			if (t.equals(vid_fish[i][0])){
				if (Double.parseDouble(vid_fish[i][2]) >= n) {
					vid_fish[i][2] = ""+(Double.parseDouble(vid_fish[i][2]) - n); 
					trum -= n;
					massGruz -=n;
					resttrum +=n;
					gruz = gruz.replaceAll(t,"");					
					System.out.println("Разгружено: "+t+" "+n+" тонн\nОстаток всего улова:"+massGruz+" тонн\n"+
					"остаток места на леднике "+resttrum+" тонн");
					for(int j=0; j<vid_fish.length; j++) {
						System.out.println("Остаток улова рыбы вида:"+vid_fish[j][0]+
								" - "+vid_fish[j][2]+" тонн");
					}
				}
			}
		}
	}
	void show() {
		
	}	
	void showfield(JTextField[] txtfield){
		
	}
}
public class Ships {
	JPanel mainPanel;
	JFrame theFrame;
	ArrayList<JCheckBox> checkboxList;
	String[] shipvid = {"Liner","Yaht","Tanker","Barge","Fishship","Aircraft","Esminec"};
	String[] img_url = {"c:/Liner3.png","c:/Yacht2.png","c:/Tanker2.png","c:/Barge2.png","c:/Fishship3.png","c:/Aircraft2.png","c:/Esminec2.png"};
	ImageIcon[] img = new ImageIcon[7];
	
	public void buildGUI_win1(){
		theFrame = new JFrame("Ships");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
		
		checkboxList = new ArrayList<JCheckBox>();
		JPanel buttonPanel = new JPanel();
		
		JButton start = new JButton("Ok");
		buttonPanel.add(start);
		JButton end = new JButton("Cansel");
		buttonPanel.add(end);
		
		for (int i=0; i<img_url.length; i++) {
			img[i] = new ImageIcon(img_url[i]);
		}
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for (int i=0; i<shipvid.length; i++) {
			nameBox.add(new JLabel(shipvid[i],img[i], JLabel.HEIGHT)); 
		}
		
		background.add(BorderLayout.SOUTH, buttonPanel);
		background.add(BorderLayout.WEST, nameBox);
		
		theFrame.getContentPane().add(background);
		
		GridLayout grid = new GridLayout(7,15);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		// флажки
		for (int i=0; i<shipvid.length; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c); 
		}
		theFrame.setBounds(50,50,300,300);
		theFrame.pack();
		theFrame.setVisible(true);
	}
	
	JFrame Win2frame;
	JPanel Win2panel;
	JTextField[] textfield;
	JPanel buttonPanel; 
	JButton[] buttons;
	
	public void buildGUI_win2(ImageIcon icon, String[] label, Ship obj, String[] button) {
		textfield = new JTextField[label.length];
		Win2frame = new JFrame("Fraht");
		Win2frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		Win2panel = new JPanel(layout);
		Box boxfield = new Box(BoxLayout.Y_AXIS);
		Box boxlabel = new Box(BoxLayout.Y_AXIS);
		JLabel vid = new JLabel("",icon,JLabel.CENTER);
		ImageIcon icLabel = new ImageIcon("c:/111111.png");
		for (int i=0; i<label.length; i++) {
			boxlabel.add(new JLabel(label[i],icLabel,JLabel.HEIGHT));
			textfield[i] = new JTextField();
			boxfield.add(textfield[i]);
		}
		obj.showfield(textfield);
		buttonPanel = new JPanel(); 
		buttons = new JButton[button.length];
		for (int i=0; i<button.length; i++) {
			buttons[i] = new JButton(button[i]);
			buttonPanel.add(buttons[i]);
		} 
		Win2panel.add(BorderLayout.NORTH,vid);
		Win2panel.add(BorderLayout.WEST,boxlabel);
		Win2panel.add(BorderLayout.EAST,boxfield);
		Win2panel.add(BorderLayout.SOUTH, buttonPanel);
		Win2frame.getContentPane().add(Win2panel);
		Win2frame.setBounds(60, 60, 300, 300);
		Win2frame.pack();
		Win2frame.setVisible(true);
	}
	public static void main(String[] args) {
		new Ships().buildGUI_win1();
		ImageIcon l = new ImageIcon("c:/Liner3.png");
		String[] namefield = {"Водоизмещение: ","Длина: ","Ширина: ","Максимальная скорость: ",
				"Пассажирских палуб: ","Кают на этаже: ","Всего пассажирских мест: ",
				"из них свободных мест: ","Зон отдыха: ","Дата отправления: ","Дата предположительного прибытия: ",
				"Курс: ","Дистанция: ","Расход топлива: ","На борту всего: "};
		String[] name_button = {"Fraht","Embarkation","Disembarkation","Cansel"};
		// Проверка Ланер
		System.out.println("ЛАЙНЕР");
		int[] s = {1000, 200, 1000};
		int[][] c = {{0,6},{10,2}}; // из Акры в Бата - Африка
		int[] dist_s = new int[2];
		int[] dist_e = new int[2];
		dist_s[0] = c[0][0];
		dist_s[1] = c[0][1];
		dist_e[0] = c[1][0];
		dist_e[1] = c[1][1];		
		Liner Lainer1 = new Liner(s,1000,100000,70,60,c,1000,4,250);
		Lainer1.dateEnd(dist_s, dist_e, 2014, 00, 23, 22, 36);
		System.out.println("Дата прибытия: "+Lainer1.dateEnd.getTime());
		System.out.println("Дистанция: "+Lainer1.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+Lainer1.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(Lainer1.fuel-Lainer1.outlayFuel(dist_s, dist_e)));
		Lainer1.show();
		Lainer1.embarkation(300);
		System.out.println("Остаток свободных мест: "+Lainer1.restplays());
		Lainer1.show();
		Lainer1.disembarkation(30);
		System.out.println("Остаток свободных мест: "+Lainer1.restplays());
		Lainer1.show();
		new Ships().buildGUI_win2(l, namefield, Lainer1,name_button);
		// Проверка Танкер
		l = new ImageIcon("c:/Tanker2.png");
		String[] namefield_T = {"Водоизмещение: ","Длина: ","Ширина: ","Максимальная скорость: ",
		"Кол-во танков: ","Eмкость танка: ","Емкость трюма: ","Остаток емкости трюма: ","Наименование груза: ",
		"Масса груза: ","Дата отправления: ","Дата предположительного прибытия: ",
		"Курс: ","Дистанция: ","Расход топлива: "};
		name_button[0] = "Fraht";
		name_button[1] = "Loading";
		name_button[2] = "Unloading";
		name_button[3] = "Cansel";
		System.out.println("ТАНКЕР");
		s[0] = 1000;
		s[1] = 200;
		s[2] = 10;
		int[][] t_c = {{107,11},{114,22}}; // из Сайгона (Вьетнам) в Сянган (Китай)
		dist_s[0] = t_c[0][0];
		dist_s[1] = t_c[0][1];
		dist_e[0] = t_c[1][0];
		dist_e[1] = t_c[1][1];	
		Tanker Tanker1 = new Tanker(s,20000,10000,70,40,t_c,1000,4);
		Tanker1.dateEnd(dist_s, dist_e, 2014, 00, 23, 22, 36);
		System.out.println("Дата прибытия: "+Tanker1.dateEnd.getTime());
		System.out.println("Дистанция: "+Tanker1.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+Tanker1.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(Tanker1.fuel-Tanker1.outlayFuel(dist_s, dist_e)));
		Tanker1.show();
		Tanker1.loading(300, "Нефть");
		Tanker1.show();
		Tanker1.unloading(100, "Нефть");
		Tanker1.show();
		new Ships().buildGUI_win2(l, namefield_T, Tanker1,name_button);
		// Проверка Авианосец
		System.out.println("АВИАНОСЕЦ");
		s[0] = 1000;
		s[1] = 200;
		s[2] = 10;
		int[][] a_c = {{23,21},{19,-34}}; // Порт-Этьен (Мавритания) в Кейптаун (ЮАР)
		dist_s[0] = a_c[0][0];
		dist_s[1] = a_c[0][1];
		dist_e[0] = a_c[1][0];
		dist_e[1] = a_c[1][1];	
		Aircraft Aircraft1 = new Aircraft(s,20000,10000,70,40,a_c,10000,20);
		Aircraft1.dateEnd(dist_s, dist_e, 2014, 00, 23, 00, 21);
		System.out.println("Дата прибытия: "+Aircraft1.dateEnd.getTime());
		System.out.println("Дистанция: "+Aircraft1.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+Aircraft1.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(Aircraft1.fuel-Aircraft1.outlayFuel(dist_s, dist_e)));
		Aircraft1.shooting(3000);
		Aircraft1.addAmmunition(1000);
		Aircraft1.take_off(11);
		Aircraft1.landing(3);
		Aircraft1.landing(15);
		// Пороверка рыболовецкое судно
		System.out.println("РЫБОЛОВЕЦКИЙ СЕЙНЕР");
		s[0] = 1000;
		s[1] = 200;
		s[2] = 10;
		int[][] f_c = {{23,21},{19,-34}}; // Порт-Этьен (Мавритания) в Кейптаун (ЮАР)
		dist_s[0] = f_c[0][0];
		dist_s[1] = f_c[0][1];
		dist_e[0] = f_c[1][0];
		dist_e[1] = f_c[1][1];	
		String[][] vid_f = {{"Тунец","400","0"},{"Лосось","400","0"},{"Форель","200","0"}};
		FishShip FishShip1 = new FishShip(s,2000,6000,70,20,f_c,1000,vid_f);
		for (int i=0; i<FishShip1.vid_fish.length; i++){
			for (int j=0; j<FishShip1.vid_fish[0].length; j++) {
				System.out.print(FishShip1.vid_fish[i][j]+" |");
			}
			System.out.println();
		}
		FishShip1.dateEnd(dist_s, dist_e, 2014, 00, 23, 00, 21);
		System.out.println("Дата прибытия: "+FishShip1.dateEnd.getTime());
		System.out.println("Дистанция: "+FishShip1.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+FishShip1.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(FishShip1.fuel-FishShip1.outlayFuel(dist_s, dist_e)));
		String[] v_f = {"0","200"};
		FishShip1.fishing(v_f);
		v_f[0] = "0";
		v_f[1] = "100";
		FishShip1.fishing(v_f);
		v_f[0] = "0";
		v_f[1] = "100";
		FishShip1.fishing(v_f);
		v_f[0] = "0";
		v_f[1] = "100";
		FishShip1.fishing(v_f);
		v_f[0] = "1";
		v_f[1] = "100";
		FishShip1.fishing(v_f);
		v_f[0] = "1";
		v_f[1] = "250";
		FishShip1.fishing(v_f);
		v_f[0] = "2";
		v_f[1] = "110";
		FishShip1.fishing(v_f);
		v_f[0] = "2";
		v_f[1] = "70";
		FishShip1.fishing(v_f);
		FishShip1.unloading(130, "Лосось");
		FishShip1.unloading(150, "Тунец");
		FishShip1.unloading(150, "Форель");
		// Пороверка ЭСМИНЕЦ
		System.out.println("ЭСМИНЕЦ");
		s[0] = 1000;
		s[1] = 200;
		s[2] = 10;
		int[][] e_c = {{23,21},{19,-34}}; // Порт-Этьен (Мавритания) в Кейптаун (ЮАР)
		dist_s[0] = e_c[0][0];
		dist_s[1] = e_c[0][1];
		dist_e[0] = e_c[1][0];
		dist_e[1] = e_c[1][1];
		Esminec Esminec1 = new Esminec(s,10000,10000,90,70,a_c,10000);
		Esminec1.dateEnd(dist_s, dist_e, 2014, 00, 23, 00, 21);
		System.out.println("Дата прибытия: "+Esminec1.dateEnd.getTime());
		System.out.println("Дистанция: "+Esminec1.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+Esminec1.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(Esminec1.fuel-Esminec1.outlayFuel(dist_s, dist_e)));
		Esminec1.shooting(3000);
		Esminec1.addAmmunition(1000);
		Esminec1.addAmmunition(5000);
		// Яхта
		System.out.println("КРУИЗНЫЙ КАТЕР \"АРГОНАВТ\"");
		s[0] = 35;
		s[1] = 7;
		s[2] = 8;
		int[][] y_c = {{23,21},{19,-34}}; // Порт-Этьен (Мавритания) в Кейптаун (ЮАР)
		dist_s[0] = y_c[0][0];
		dist_s[1] = y_c[0][1];
		dist_e[0] = y_c[1][0];
		dist_e[1] = y_c[1][1];
		Yacht Yacht_Argonaft = new Yacht(s,80,1000,50,25,y_c,15,1);
		Yacht_Argonaft.dateEnd(dist_s, dist_e, 2014, 00, 23, 00, 21);
		System.out.println("Дата прибытия: "+Yacht_Argonaft.dateEnd.getTime());
		System.out.println("Дистанция: "+Yacht_Argonaft.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+Yacht_Argonaft.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(Yacht_Argonaft.fuel-Yacht_Argonaft.outlayFuel(dist_s, dist_e)));
		System.out.println("Вид яхты - "+Yacht_Argonaft.v_yacht);
		System.out.println("Кол-во пассажирских мест - "+Yacht_Argonaft.passenger);
		System.out.println("Кол-во пассажиров: "+Yacht_Argonaft.restpassenger);
		Yacht_Argonaft.embarkation(5);
		Yacht_Argonaft.embarkation(10);
		Yacht_Argonaft.disembarkation(3);
		System.out.println("Кол-во пассажиров: "+Yacht_Argonaft.restpassenger);
		System.out.println("Остаток мест: "+Yacht_Argonaft.restplays());
		// Баржа
		System.out.println("БАРЖА");
		l = new ImageIcon("c:/Barge2.png");
		String[] namefield_B = {"Водоизмещение: ","Длина: ","Ширина: ","Максимальная скорость: ",
		"Емкость трюма: ","Остаток емкости трюма: ","Наименование груза: ",
		"Масса груза: ","Дата отправления: ","Дата предположительного прибытия: ",
		"Курс: ","Дистанция: ","Расход топлива: "};
		name_button[0] = "Fraht";
		name_button[1] = "Loading";
		name_button[2] = "Unloading";
		name_button[3] = "Cansel";
		s[0] = 100;
		s[1] = 20;
		s[2] = 8;
		int[][] b_c = {{23,21},{19,-34}}; // Порт-Этьен (Мавритания) в Кейптаун (ЮАР)
		dist_s[0] = b_c[0][0];
		dist_s[1] = b_c[0][1];
		dist_e[0] = b_c[1][0];
		dist_e[1] = b_c[1][1];
		Barge Barge1 = new Barge(s,180,1000,50,10,b_c,1000);
		Barge1.dateEnd(dist_s, dist_e, 2014, 00, 23, 00, 21);
		System.out.println("Дата прибытия: "+Barge1.dateEnd.getTime());
		System.out.println("Дистанция: "+Barge1.distance(dist_s, dist_e));
		System.out.println("Расход топлива: "+Barge1.outlayFuel(dist_s, dist_e));
		System.out.println("Остаток топлива: "+(Barge1.fuel-Barge1.outlayFuel(dist_s, dist_e)));
		System.out.println("Груз баржи - "+Barge1.gruz);
		Barge1.show();
		Barge1.loading(300, "Гравий");
		Barge1.show_trum();
		Barge1.show();
		Barge1.loading(200, "Песок");
		Barge1.show_trum();
		Barge1.show();
		Barge1.loading(300, "Гравий");
		Barge1.show_trum();
		Barge1.show();
		Barge1.unloading(600,"Гравий");
		Barge1.show_trum();
		Barge1.show();
		Barge1.unloading(200,"Песок");
		Barge1.show_trum();
		Barge1.show();
		new Ships().buildGUI_win2(l, namefield_B, Barge1,name_button);
	}
}
