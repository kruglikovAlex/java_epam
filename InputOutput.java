import java.util.*;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
//import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.file.*;
import java.nio.channels.*;
//import java.nio.channels.FileChannel.MapMode;
import java.awt.*;
import java.awt.event.*;

//import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class InputOutput {
	boolean fileOk;
	static File file;
	File to_file;
	static FileReader fr;
	FileWriter fw;
	BufferedReader br;
	BufferedWriter bw;
	LineNumberReader lnr;
	CharArrayWriter caw = new CharArrayWriter();
	PrintWriter pw;
	RandomAccessFile raf;
	String text, temp, message, resultStr="";
	String str[], words[];
	StringBuffer sbuf;
	StringTokenizer sToken;
	TextView tv = new TextView(resultStr,null,null,0);
	Frame ftv = tv;
	
	// ====== java.io ============
	// ��������������� ����� ������������ ����������
	protected static void fail(String msg) throws IllegalArgumentException {
		throw new IllegalArgumentException(msg);
	}
	private static void abort(String msg) throws IOException {
		throw new IOException("������ ������ � �������� ����: "+msg);
	}
	// �������� ������� ��������� �����
	public boolean checkInFile(String path, String File_n) throws IOException {
		file = new File(path+File_n);
		// ���� ����������
		if (!file.exists()) {
			fileOk = false;
			fail("��������: ��� ������ �����: "+path+File_n);
		}
		if (!file.isFile()) {
			fileOk = false;
			fail("��������: ���������� ������ �������� "+path+File_n+", ������� ����");
		}
		if (!file.canRead()) {
			fileOk = false;
			fail("��������: �������� ���� �� �������� ��� ������ "+path+File_n);
		}
		fileOk = true;
		return fileOk;
	}
	// �������� ������� "���������" �����
	public boolean checkOutFile(String path, String File_n) throws IOException {
		to_file = new File(path+File_n);
		// ���� "�������� ����" - �������, �� � �������� ����� 
		// ������������ ��� ��������� ����� 
		if (to_file.isDirectory()) to_file = new File(to_file, "\\"+file.getName());
		// ���� �������� ���� ���������� - �������� �� ������ ��� ������
		// ���� �������� - ����������� ���������� ������������. ���� �������� ����
		// �� ���������� - ���������� �� ������� � �������� ��� ������
		System.out.println("to_file - "+to_file.getParent()+"\\"+to_file.getName());
		if (to_file.exists()){
			if (!to_file.canWrite())
				abort("�������� ���� �� �������� ��� ������ "+path+File_n);
			// ������ �� ����������
			System.out.print("������������ ������������ ���� "+to_file.getName()+"? (Y/N): ");
			System.out.flush();
			// �������� ����� ������������
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				String response = in.readLine();
				// �������� ������
				if (!response.equals("Y")&&!response.equals("y"))   //��� !response.equalsIgnoreCase("y")
					abort("������������ ���� �� ��� �����������");
				else createFile(path, File_n); 
			} catch (IOException e) {
				message = "Error I/O - ������ �����"+e;
				JOptionPane.showMessageDialog(null, message,"���������� - IOException",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			// ���� ����� ���, �������� �������� � ����� ������ � ������ � ���� ��� 
			// ������. ���� getParent() ������ null, �.�. ������������� �������� ���
			// ��������� ����� ������� dir., ��� ��� ����������� ���������� user.dir.
			String parent = to_file.getParent(); // ������� ����������
			if (parent == null) // ���� ������ �������� ���, ���������� �������
				parent = System.getProperty("user.dir");
			File dir = new File(parent); // ����������� � ����
			if (!dir.exists())
				abort("������� ���������� �����������: "+parent);
			if (dir.isFile())
				abort("������� ���������� �� �������� ���������: "+parent);
			if (dir.canWrite())
				abort("������� ���������� �� �������� ��� ������"+parent);
		}
		return fileOk;
	}
	// �������� ����� ��� ������ FileReader()->BufferedReader()
	public BufferedReader in_BufferedReader(String path, String File_n){
		resultStr = "�������� ����� ��� ������ � �������:\nFileReader()->BufferedReader()\n";
		tv.textarea.append(resultStr);
		try {
			file = new File(path+File_n);
			if (file.exists()){
				if (file.isFile()){
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					resultStr += "�������� ����� ��� ������ ����������� �������.\n";
				}
			}
			tv.textarea.append(resultStr);
		}
		catch (FileNotFoundException e){
			message = "���� �� ������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - FileNotFoundException",JOptionPane.ERROR_MESSAGE);
		}
		return br;
	}
	// �������� ����� ��� ������ FileReader()->LineNumberReader()
	public int in_LineNumberReader(String path, String File_n){
		resultStr = "�������� ����� ��� ������ � �������:\nFileReader()->LineNumberReader()\n";
		tv.textarea.append(resultStr);
		int count_str = 0;
		text = "";
		temp = "";
		try {
			file = new File(path+File_n);
			if (file.exists()){
				if (file.isFile()){
					fr = new FileReader(file);
					lnr = new LineNumberReader(fr);
					while ((temp=lnr.readLine())!=null) {
						text +=temp+"\n";
					}
				count_str = lnr.getLineNumber();
				resultStr = "count_str->"+count_str+"\n";
				tv.textarea.append(resultStr);
				lnr.close();
				}
			}	
		} 
		catch (FileNotFoundException e){
			message = "���� �� ������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - FileNotFoundException", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e) {
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
		return count_str;
	}
	// ������ ����� BufferedReader()
	public String readTxt(BufferedReader f){
		resultStr = "������ ����� � ������� BufferedReader():\n";
		tv.textarea.append(resultStr);
		text = "";
		int c;
		try {
			while ((c=f.read())!=-1){
				text += (char)c;
			}
			resultStr = "������ � ���������� text ����������� �������.\n";
			tv.textarea.append(resultStr);
			f.close();
		}
		catch (IOException e){
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
		return text;
	}
	// �������� ����� ��� ������ FileReader()->Scanner()
	public String in_Scanner(String path, String File_n){
		resultStr = "�������� ����� ��� ������ � �������:\nFileReader()->Scanner()\n";
		tv.textarea.append(resultStr);
		text = "";
		try {
			fr = new FileReader(path+File_n);
			Scanner scan = new Scanner(fr);
			resultStr = "������ �� ����� - "+File_n+":\n";
			tv.textarea.append(resultStr);
			while (scan.hasNext()){
				text += scan.nextLine()+"\n"; 
			}
			resultStr = "������ � ���������� text ����������� �������\n";
			tv.textarea.append(resultStr);
			scan.close();
			fr.close();
		} catch (FileNotFoundException e){
			message = "���� �� ������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - FileNotFoundException", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
		return text;
	}
	// ==================
	// �������� ������ ������ �� �����
	public void close_R(Reader...args){
		try {
			for (int i=0; i<args.length; i++){
				args[i].close();
			}
			resultStr = "�������� ������ ������ - ����� close_R(Reader...args)\n";
			tv.textarea.append(resultStr);
		}
		catch (IOException e){
			message = "������ �������� �������:"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	//  �������� ������ ������ � ����
	public void close_W(Writer...args){
		try {
			for (int i=0; i<args.length; i++){
				args[i].close();
			}
			resultStr = "�������� ������ ������ - ����� close_W(Reader...args)\n";
			tv.textarea.append(resultStr);
		}
		catch (IOException e){
			message = "������ �������� �������:"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// ==================
	// ������ ������ � ���� - FileWriter()
	public void out_FileWriter(String path, String File_name, boolean add){
		resultStr = "������ � ���� ����� FileWriter()\n";
		tv.textarea.append(resultStr);
		try {
			file = new File(path+File_name);
			if (file.canWrite()){
				fw = new FileWriter(file, add);
				fw.write(text);
				fw.close();
				resultStr = "������ � ���� - "+File_name+" ������ �������.\n";
				tv.textarea.append(resultStr);
			} else JOptionPane.showMessageDialog(null, "������ �����/������ - ���� ������ ������ ��� ������","", JOptionPane.ERROR_MESSAGE);
		}catch (IOException e){
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// ������ ������ � ���� - CharArrayWriter()
	public void out_CharArrayWriter(String path, String File_name){
		char buf[] = new char[text.length()];
		text.getChars(0, text.length(), buf, 0);
		resultStr = "������ � ���� ����� CharArrayWriter()\n";
		tv.textarea.append(resultStr);
		for (int i=0; i<buf.length; i++){
			text += buf[i];
		}
		try{
			caw.write(buf);
			resultStr = "\n������ � ����� ������ �������.\n";
			tv.textarea.append(resultStr);
		} 
		catch (IOException e) {
			message = "������ ������ � �����"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			file = new File(path+File_name);
			FileWriter fwr = new FileWriter(file);
			caw.writeTo(fwr);
			fwr.close();
			resultStr = "\n������ � ���� - FileWriter() ->\n ������ �������.\n";
			tv.textarea.append(resultStr);
		}
		catch (IOException e){
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
		resultStr = "\n���������� reset()\n";
		tv.textarea.append(resultStr);
		caw.reset();
		close_R(br, fr);
	}	
	// ������ ������ � ���� - BufferredWriter()
	public void out_BufferedWriter(String path, String File_name, boolean add){
		resultStr = "������ � ���� ����� BufferedWriter():\n";
		tv.textarea.append(resultStr);
		try {
			file = new File(path+File_name);
			if (file.exists()){
				if (file.isFile()){
					if (file.canWrite()){
						bw = new BufferedWriter(new FileWriter(file, add));
						bw.write(text);
						bw.close();
						resultStr = "������ � ���� - "+File_name+" ������ �������\n";
						tv.textarea.append(resultStr);
					}
				}
			} else {
				bw = new BufferedWriter(new FileWriter(file, add));
				bw.write(text);
				bw.close();
				resultStr = "������ � ���� - "+File_name+" ������ �������\n";
				tv.textarea.append(resultStr);
			}
		} catch (IOException e){
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// ������ ������ � ���� - PrintWriter()
	public void out_PrintWriter(String path, String File_name){
		resultStr = "������ � ���� ����� PrintWriter():\n";
		tv.textarea.append(resultStr);
		file = new File(path+File_name);
		try {
			pw = new PrintWriter(file);
			pw.print(text);
			pw.close();
			resultStr = "������ � ���� - "+File_name+" ������ �������\n";
			tv.textarea.append(resultStr);
		} catch (FileNotFoundException e){
			message = "���� �� ������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - FileNotFoundException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// ������ ������ � ���� - FileWriter()->BufferedWriter()->PrintWriter()
	public void out_UniversalWriter(String path, String File_name, boolean add){
		resultStr = "������ � ���� ����� FileWriter()->BufferedWriter()->PrintWriter():\n";
		tv.textarea.append(resultStr);
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(path+File_name), add)));
			pw.print(text);
			pw.close();
			resultStr = "������ � ���� - "+File_name+" ������ �������\n";
			tv.textarea.append(resultStr);
		}catch (IOException e){
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// ==================
	// ������/������ � ����
	// ������������ ������ ����� RandomAccessFile()
	public void inOut_RandomAccessFile(String path, String File_name, String mode){
		try {
			resultStr = "������/������ � ���� ����� RandomAccessFile():\n";
			tv.textarea.append(resultStr);
			raf = new RandomAccessFile(path+File_name, mode);
			if (mode.equals("r")) {
				text = "";
				temp = "";
				resultStr = "����� RandomAccessFile() -> ������:\n";
				while ((temp = raf.readLine()) != null){
					text += temp+"\n";
				}
				resultStr += "������ �� ����� - "+File_name+" � ���������� text ������ �������\n";
				tv.textarea.append(resultStr);
			} else {
				resultStr = "����� RandomAccessFile() -> ������:\n";
				tv.textarea.append(resultStr);
				raf.writeBytes(text);
				resultStr = "������ �� ���������� text � ���� - "+File_name+" ������ �������\n";
				tv.textarea.append(resultStr);
			}
			raf.close();
		} catch (FileNotFoundException e){
			message = "���� �� ������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - FileNotFoundException", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e){
			message = "������ �����/������"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// ====== java.nio ====
	// ������ �� �����, ��������� ����� �����-������
	public String ChannelRead(String path, String File_name){
		resultStr = "Java.nio - ������ �� �����, ��������� ����� �����-������\n";
		tv.textarea.append(resultStr);
		int count;
		Path filepath = null;
		// �������� ���� � �����
		try {
			filepath = Paths.get(path+File_name);
			resultStr = "���� � ����� -"+filepath.getFileName()+" �������\n";
			tv.textarea.append(resultStr);
		} catch (InvalidPathException e) {
			message = "Path Error"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
		}
		// �������� ����� � ����� �����
		try {
			SeekableByteChannel fChan = Files.newByteChannel(filepath);
			resultStr = "����� ��� ������ "+fChan.toString()+" �������.\n";
			tv.textarea.append(resultStr);
			// ��������������� �����
			ByteBuffer mBuf = ByteBuffer.allocate(128);
			resultStr = "����� - "+mBuf.toString()+" �������� 128 ���� ��������������.\n";
			tv.textarea.append(resultStr);
			text = "";
			resultStr = "������ � ������:...\n";
			tv.textarea.append(resultStr);
			do {
				// ������ � �����
				//  � count ������� ���-�� ����������� ����
				count = fChan.read(mBuf);
				// ������������ ��� ���������� ����� �����
				if (count !=-1){
					// ����������� ����� ��� ������, �.�.
					// ��������� ������� ����� � ����� ����� ������
					mBuf.rewind();
					// ������ ����� �� ������ � ���������� �� � 
					// ���������� text
					for (int i=0; i<count; i++){
						text += (char)mBuf.get();
					}
				}
			} while (count !=-1);
			mBuf.clear();
			fChan.close();
			resultStr = "������ ������ �������\n";
			tv.textarea.append(resultStr);
		} catch (IOException e) {
			message = "I/O Error"+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
		return text;
	}
	// ������ �� ����� ��������� ������������� ����� � ������
	public String ChannelBufferRead(String path, String File_name){
		resultStr = "Java.nio - ������ �� ����� ��������� ������������� ����� � ������\n";
		tv.textarea.append(resultStr);
		// �������� ����� ��� �����
		try {
			FileChannel fChan = (FileChannel)Files.newByteChannel(Paths.get(path+File_name));
			resultStr = "����� �������.\n";
			tv.textarea.append(resultStr);
			// �������� ������ �����
			long fSize = fChan.size();
			resultStr = "������� ������ ������ - "+fSize+"\n";
			tv.textarea.append(resultStr);
			// �������������� ����� � �������
			MappedByteBuffer mBuf = fChan.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
			resultStr = "����� � ������� ������������.\n������ �� ������:...\n";
			tv.textarea.append(resultStr);
			// ������ �� ������
			text = "";
			for (int i=0; i<fSize; i++){
				text += (char)mBuf.get(); 
			}
			resultStr = "\n����� ��������.\n";
			tv.textarea.append(resultStr);
			mBuf.clear();
			fChan.close();
		} catch (InvalidPathException e){
			System.out.println("Path Error "+e);
		}
		catch (IOException e){
			System.out.println("I/O Error "+e);
		}
		return text;
	}
	// ====================
	// ������� ���-�� ����� � ������ � �������� ������� �����
	public String[] mas_str(int...args) {
		resultStr = "������� ���-�� ����� � ������ � �������� ������� �����\n";
		resultStr += "������� - mas_str(int...args)\n";
		tv.textarea.append(resultStr);
		int a = args.length;
		StringTokenizer t = new StringTokenizer(text);
		if (a==0){
			int count_str = 0;
			// ���-�� ����� � ������ 
			while (t.hasMoreTokens()){
				temp = t.nextToken("\n");
				count_str++;
			}
			resultStr = "���-�� ����� � ������ (count_str): "+count_str+"\n";
			tv.textarea.append(resultStr);
			str = new String[count_str];
		} else {
			str = new String[args[0]];
		}
		t = new StringTokenizer(text);
		int count = 0;
		// ������ �����
		while (t.hasMoreTokens()){
			str[count] = t.nextToken("\n");
			count++;
		}
		// �������� ������� ����� 
		resultStr = "�������� ������� �����:\n";
		tv.textarea.append(resultStr);
		for (int i=0; i<str.length; i++){
			resultStr = "������ �["+i+"] - "+str[i]+"\n";
			tv.textarea.append(resultStr);
		}
		return str;
	}
	// �������� ��������� �� ������ ������ ������
	public String delStr(String subStr){
		mas_str();
		// �������� ��������� �� ������ ������ ������
		resultStr = "�������� ��������� �� ������ ������ ������ - ����� String delStr(String subStr):\n";
		tv.textarea.append(resultStr);
		StringBuffer txt;
		text = "";
		//System.out.println("�������� ��������� �� ������ ������ ������\n");
		for (int i=0; i<str.length; i++) {
			txt = new StringBuffer(str[i]);
			int start = 0, end = 0;
			start = txt.indexOf(subStr);
			resultStr ="������� ��������� � ������ �"+i+":\nstart: "+start+"\n";
			tv.textarea.append(resultStr);
			end = start+subStr.length();
			resultStr ="end: "+end+"\n";
			tv.textarea.append(resultStr);
			if ((start!=-1)&(end!=-1)){
				txt.delete(start, end);	
			}
			text += txt.toString()+"\n";
		}
		resultStr = "��������� (text):\n"+text+"\n";
		tv.textarea.append(resultStr);
		return text;
	}
	// ������ ��������� � ������ ������ ������ �������� ����������
	public String change_str(String txt_str, String change_str){
		resultStr = "������ ��������� � ������ ������ ������ �������� ����������\n";
		resultStr += "������� - change_str(String txt_str, String change_str)\n";
		tv.textarea.append(resultStr);
		mas_str();
		// ������ ��������� txt_str �� change_str
		StringBuffer txt;
		text = "";
		int start, end;
		//System.out.println("������ ��������� � ������ ������ ������ ����� ����������\n");
		for (int i=0; i<str.length; i++) {
			txt = new StringBuffer(str[i]);
			start = 0;
			end = 0;
			start = txt.indexOf(txt_str);
			resultStr = "������� ��������� � ������ �"+i+":\nstart: "+start+"\n";
			tv.textarea.append(resultStr);
			//System.out.println("������� ��������� � ������ �"+i+":\nstart: "+start);
			end = start+txt_str.length();
			resultStr = "end: "+end+"\n";
			tv.textarea.append(resultStr);
			//System.out.println("end: "+end);
			if ((start!=-1)&(end!=-1)){
				txt.replace(start, end, change_str);
			}
			text += txt.toString()+"\n";
		}
		resultStr = "��������� (text):\n"+text+"\n";
		tv.textarea.append(resultStr);
		//System.out.println("��������� (text):\n"+text);
		return text;
	}
	// ����� ���� � ������ ������, ������������ � ������� �����
	public String glasnLitters(String path, String File_n){
		resultStr = "����� ���� � ������ ������, ������������ � ������� �����\n";
		resultStr += "������� - glasnLitters(String path, String File_n)\n";
		tv.textarea.append(resultStr);
		StringTokenizer t;
		mas_str(in_LineNumberReader(path, File_n));
		char glasn[] = {'�','�','�','�','�','�','�','�','�'};
		temp = "";
		text = "";
		for (int i=0; i<str.length; i++){
			t  = new StringTokenizer(str[i]);
			text += "\n��������� "+i+"-"+str[i]+"\n����� �� �������: ";
			resultStr = "\n��������� "+i+"-"+str[i]+"\n����� �� �������: ";
			tv.textarea.append(resultStr);
			while (t.hasMoreTokens()){
				temp = t.nextToken(" .,:;!?-");
				for (int j=0; j<glasn.length; j++){
					if (temp.charAt(0)==glasn[j]){
						text += temp+" ";
						resultStr = temp+" ";
						tv.textarea.append(resultStr);
						break;
					}
				}
			}
			text += "\n";
			resultStr = "\n";
			tv.textarea.append(resultStr);
		}
		return text;
	}
	// ����� � ����� ���� ������ � ������� ��������� �����
	// ������ ����� ��������� � ������ ������ ���������� �����
	public String endStartLitters(){
		resultStr = "����� � ����� ���� ������ � ������� ��������� �����\n������ ����� ��������� � ������ ������ ���������� �����\n";
		resultStr += "������� - endStartLitters()";
		tv.textarea.append(resultStr);
		StringTokenizer t = new StringTokenizer(text);
		int c = 0;
		while(t.hasMoreTokens()){
			temp = t.nextToken(" :;,.!?-\n");
			c++;
		}
		words = new String[c];
		t = new StringTokenizer(text);
		c = 0;
		while(t.hasMoreTokens()){
			words[c] = t.nextToken(" :;,.!?-\n");
			c++;
		}
		for (int i=0; i<words.length; i++){
			resultStr = "words-������["+i+"]-"+words[i]+"\n";
			tv.textarea.append(resultStr);
			//System.out.println("words-������["+i+"]-"+words[i]);
		}
		temp = "\n���������:\n";
		resultStr = "���������:\n";
		tv.textarea.append(resultStr);
		for (int i=1; i<words.length; i++){
			if (words[i-1].charAt(words[i-1].length()-1)==words[i].charAt(0)){
				temp += words[i-1]+"+"+words[i]+"\n";
				resultStr = words[i-1]+"+"+words[i]+"\n";
				tv.textarea.append(resultStr);
			}
		}	
		return text += temp;
	}
	// ����� � ������ ����������� ����� ����, ������ ������
	public String findNumber(){
		resultStr = "����� � ������ ����������� ����� ����, ������ ������\n";
		resultStr += "������� - findNumber()";
		tv.textarea.append(resultStr);
		char alf_r[] = new char[32];
		char alf_e[] = new char[26];
		// ������� �������
		char s = '�';
		for (int i=0; i<alf_r.length; i++){
			alf_r[i] = s;
			s++;
		}
		// ���������� �������
		s = 'a';
		for (int i=0; i<alf_e.length; i++){
			alf_e[i] = s;
			s++;
		}
		String rus = new String(alf_r);
		String eng = new String(alf_e);
		// ���-�� ����������� �������� � ������
		StringTokenizer t = new StringTokenizer(text);
		int count = 0;
		while (t.hasMoreTokens()){
			temp = t.nextToken(rus.concat(eng).concat(" .,;:!-?"));
			count++;
		}
		// ����������� ��������� � ������ �����
		t = new StringTokenizer(text);
		String nLetters[] = new String[count];
		count=0;
		while (t.hasMoreTokens()){
			nLetters[count] = t.nextToken(rus.concat(eng).concat(" .,;:!-?"));
			count++;
		}
		// ���������� ������ max ����������� ���������
		String maxSubString = nLetters[0];
		for (int i=1; i<nLetters.length; i++){
			if (maxSubString.length()<nLetters[i].length()){
				maxSubString = nLetters[i];
			}
		}
		text = text.concat("\n���������� �������� ��������� - "+maxSubString);
		resultStr = "���������� �������� ��������� - "+maxSubString+"\n";
		tv.textarea.append(resultStr);
		return text;
	}
	// ������� ������������� ���� �� ������ � ������ ������ ������
	public String countWords(String path, String File_n, String[] spis){
		resultStr = "���������� ������� ������������� ���� �� ������ � ������ ������ ������\n";
		resultStr += "������� - countWords(String path, String File_n, String[] spis)\n";
		tv.textarea.append(resultStr);
		StringTokenizer t;
		mas_str(in_LineNumberReader(path, File_n));
		int count;
		for (int i=0; i<str.length; i++){
			count = 0;
			text += "������ "+i+"-"+str[i]+"\n������� ��������� ���� �� ������:\n";
			resultStr = "������ "+i+"-"+str[i]+"\n������� ��������� ���� �� ������:\n";
			tv.textarea.append(resultStr);
			for (int j=0; j<spis.length; j++){
				t  = new StringTokenizer(str[i]);
				count = 0;
				while (t.hasMoreTokens()){
					temp = t.nextToken(" .,:;!?-");
					if (temp.compareTo(spis[j])==0){
						count++;
					}
				}
				text += "����� - "+spis[j]+" ����������� "+count+" ���\n";
				resultStr = "����� - "+spis[j]+" ����������� "+count+" ���\n";
				tv.textarea.append(resultStr);
				count = 0;
			}
			text += "\n";
			count = 0;
		}
		//System.out.println("��������: "+text);
		resultStr = "�������� ������ ��� ������: "+text+"\n";
		tv.textarea.append(resultStr);
		return text;
	}
	// ������ ��������� ���� ����������
	public String changeLetters(){
		resultStr = "������ ��������� ���� ���������� ������� changeLetters()\n";
		tv.textarea.append(resultStr);
		StringTokenizer t = new StringTokenizer(text);
		temp = "";
		text = "";
		while (t.hasMoreTokens()){
			temp = t.nextToken(" \n");
			text += temp.substring(0,1).toLowerCase()+temp.substring(1, temp.length())+" ";
		}
		resultStr = "�������� ������ ��� ������: "+text+"\n";
		tv.textarea.append(resultStr);
		return text;
	}
	// ������� ���-�� �������� ���� � ���� � ������
	public String countPovtorLittersWords(){
		resultStr = "������� ���-�� �������� ���� � ���� � ������\n";
		resultStr += "������� - countPovtorLittersWords()\n";
		tv.textarea.append(resultStr);
		StringTokenizer t = new StringTokenizer(text);
		String words[][];
		// ������� ���-�� ���� � ������
		int count = 0;
		while (t.hasMoreTokens()){
			temp = t.nextToken(" .,:;-!?");
			count++;
		}
		words = new String[count][2];
		count = 0;
		t = new StringTokenizer(text);
		while (t.hasMoreTokens()){
			words[count][0] = t.nextToken(" .,:;-!?");
			words[count][1] = "1";
			count++;
		}
		count = 1;
		// ������� ���-�� �������� ���� � ������
		for (int i=0; i<words.length; i++) {
			for (int j=1+i; j<words.length; j++){
				if ((words[j][0].compareTo("null")!=0)&
						(words[i][0].compareTo("null")!=0)&
							(words[i][0].compareTo(words[j][0])==0))
				{
					count++;
					words[i][1] = ""+count;
					words[j][0] = "null";
				}
			}
			count = 1;
		}
		// ������� ���-�� �������� ���� � ������
		// 	�������
		String alf_re[][] = new String[58][2];
		// ������� �������
		char s = '�';
		for (int i=0; i<32; i++){
			alf_re[i][0] = ""+s;
			alf_re[i][1] = "0";
			s++;
		}
		// ���������� �������
		s = 'a';
		for (int i=32; i<58; i++){
			alf_re[i][0] = ""+s;
			alf_re[i][1] = "0";
			s++;
		}
		count = 0;
		for (int i=0; i<alf_re.length; i++){
			for (int j=0; j<text.length(); j++){
				if (text.charAt(j) == alf_re[i][0].charAt(0)){
					count++;
					alf_re[i][1] = ""+count;
				}
			}
			count = 0;
		}
		// �������� words
		text += "\n���������:\n�����:\n";
		resultStr = "\n���������:\n�����:\n";
		tv.textarea.append(resultStr);
		//System.out.println("��������:");
		for (int i=0; i<words.length; i++){
			if ((words[i][0].compareTo("null")!=0)&(words[i][0].compareTo("\n ")!=0)){
				text += "����� - "+words[i][0]+" ����������� - "+words[i][1]+" ���(�)\n";
				resultStr = "����� - "+words[i][0]+" ����������� - "+words[i][1]+" ���(�)\n";
				tv.textarea.append(resultStr);
			}
		} 
		text += "�����:\n";
		resultStr = "�����:\n";
		tv.textarea.append(resultStr);
		for (int i=0; i<alf_re.length; i++){
			text += "����� - "+alf_re[i][0]+" ����������� - "+alf_re[i][1]+" ���(�)\n";
			resultStr = "����� - "+alf_re[i][0]+" ����������� - "+alf_re[i][1]+" ���(�)\n";
			tv.textarea.append(resultStr);
		}
		//System.out.println("text:\n"+text);
		return text;
	}
	// ������ �������� �����
	public void reversSymb(String path_in, String File_name_in, String path_out, String File_name_out) {
		resultStr = "������ �������� ����� ������� -reversSymb(String path_in, String File_name_in, String path_out, String File_name_out)\n";
		tv.textarea.append(resultStr);
		//System.out.println("������� ����� - reversSymb():");
		try {
			checkInFile(path_in, File_name_in);
			if (fileOk == true) {
				checkOutFile(path_out, File_name_out);
				if (fileOk == true){
					br = new BufferedReader(new FileReader(path_in+File_name_in));
					String reversTxt;
					StringBuffer bTxt;
					text = "";
					resultStr = "������ � ������:...\n.";
					tv.textarea.append(resultStr);
					while ((reversTxt = br.readLine())!= null) {
						bTxt = new StringBuffer(reversTxt);
						text += bTxt.reverse().toString()+"\n";
					}
					resultStr = "������ ���������� � ���� - "+File_name_out+"\n";
					tv.textarea.append(resultStr);
					out_UniversalWriter(to_file.getParent(), to_file.getName(), false);
					// ������� ������ FileViewer
					Frame ffv = new FileViewer(to_file.getParent(),to_file.getName(),0);
					ffv.setVisible(true);
				}
			}
		} catch (IOException e){
			System.err.println(e.getMessage());
		}
	}
	// ������ �������� ����� � �������������� java.nio - �������� ������� I/O
	public void reversSymbNio(String path_in, String File_name_in, String path_out, String File_name_out) {
		resultStr = "������ �������� ����� � �������������� java.nio - �������� ������� I/O\n";
		resultStr += "������� - reversSymbNio(String path_in, String File_name_in, String path_out, String File_name_out)\n";
		tv.textarea.append(resultStr);
		//System.out.println("������� ����� - reversSymbNio():");
		try {
			checkInFile(path_in, File_name_in);
			if (fileOk == true) {
				checkOutFile(path_out, File_name_out);
				if (fileOk == true){
					// ������ � ������
					int count;
					Path filepath_in = null;
					Path filepath_out = null;
					// �������� ���� � �����
					try {
						filepath_in = Paths.get(file.toString());
						filepath_out = Paths.get(to_file.toString());
						resultStr = "�������� ���� � ���. ����� - "+file.getName()+" � ��������� - "+to_file.getName()+"\n";
						tv.textarea.append(resultStr);
					} catch (InvalidPathException e){
						System.out.println("Path error"+e.getMessage());
						return;
					}
					// �������� ������
						// ����� ��� ������
					SeekableByteChannel fChan_in = Files.newByteChannel(filepath_in);
						// ����� ��� ������
					SeekableByteChannel fChan_out = Files.newByteChannel(filepath_out, 
																		StandardOpenOption.WRITE,
																		StandardOpenOption.CREATE);
					resultStr = "�������� ����� ��� ������ - "+fChan_in+" � ������ - "+fChan_out+"\n";
					tv.textarea.append(resultStr);
					// ����������� �����
					ByteBuffer mBuf_in = ByteBuffer.allocate(128);
					ByteBuffer mBuf_out = ByteBuffer.allocate(128);
					resultStr = "�������� ������ ��� ������ mBuf_in - "+mBuf_in.toString()+" � ������ - mBuf_out - "+mBuf_in.toString()+", �������� 128 ����\n";
					tv.textarea.append(resultStr);
					StringBuffer sBuf = null;
					text = "";
					resultStr = "������ � ������:\n";
					tv.textarea.append(resultStr);
					do {
						// ������ � �����
						count = fChan_in.read(mBuf_in);
						resultStr = "������ � �����, ���-�� ����������� ���� - "+count+"\n";
						tv.textarea.append(resultStr);
						// ���� ��� ���������� ����� �����
						//System.out.println("count - "+count);
						if (count != -1){
							// �������������� ����� ��� ������ 
							mBuf_in.rewind();
							// ������ ����� �� ������ � �������� ������� 
							// � ���������� � �������� �����
							for (int i=0; i<count; i++){
								text += (char)mBuf_in.get();
							}
							//System.out.println("text - "+text);
							sBuf = new StringBuffer(text);
							text = sBuf.reverse().toString();
							mBuf_out.rewind();
							resultStr = "�������� � �������� ����� - "+count+" ����������� ����\n";
							tv.textarea.append(resultStr);
							for (int i = 0; i < count; i++)
						         mBuf_out.put((byte)text.charAt(i));
							mBuf_out.limit(count);
							mBuf_out.rewind();
							fChan_out.write(mBuf_out);
							mBuf_out.clear();
							mBuf_in.clear();
						}
						text = "";
					} while (count !=-1);
					fChan_in.close();
					fChan_out.close();
					resultStr = "��������� ��������� � �������� ����� -"+filepath_out.getFileName()+"\n";
					tv.textarea.append(resultStr);
					// ������� ������ FileViewer
					Frame ff = new FileViewer(filepath_out.getParent().toString(),filepath_out.getFileName().toString(),0);
					// ������� ����
					ff.setVisible(true);
				}
			}	
		} catch (IOException e){
			System.err.println(e.getMessage());
		} // �������� finally ??????????????????????
	}
	// ������ �������� ����� � �������������� java.nio - I/O ����� ������������� 
	public void reversSymbNio2(String path_in, String File_name_in, String path_out, String File_name_out) {
		resultStr = "������ �������� ����� � �������������� java.nio - I/O ����� �������������\n";
		resultStr += "������� - reversSymbNio2(String path_in, String File_name_in, String path_out, String File_name_out)\n";
		tv.textarea.append(resultStr);
		try {
			checkInFile(path_in, File_name_in);
			if (fileOk == true) {
				checkOutFile(path_out, File_name_out);
				if (fileOk == true){
					Path filepath_in = null;
					Path filepath_out = null;
					// �������� ���� � �����
					try {
						filepath_in = Paths.get(file.toString());
						filepath_out = Paths.get(to_file.toString());
						resultStr = "���� � ����� ������� (Paths.get())\n";
						tv.textarea.append(resultStr);
					} catch (InvalidPathException e){
						System.out.println("Path error"+e.getMessage());
						return;
					}
					// �������� ����� ��� �����
					try {
						FileChannel fChan_in = (FileChannel)Files.newByteChannel(filepath_in,
																					StandardOpenOption.READ);
						FileChannel fChan_out = (FileChannel)Files.newByteChannel(filepath_out, 
																					StandardOpenOption.WRITE,
																					StandardOpenOption.READ,
																					StandardOpenOption.CREATE);
						resultStr = "�������� ����� ��� ������ - "+fChan_in+" � ������ - "+fChan_out+"\n";
						tv.textarea.append(resultStr);
						// �������� ������ �����
						long fSize_in = fChan_in.size();
						resultStr = "�������� ������ ����� ��� ������� ������� ������ - "+fSize_in+"\n";
						tv.textarea.append(resultStr);
						// ������������� ����� � �������
						MappedByteBuffer mBuf_in = fChan_in.map(FileChannel.MapMode.READ_ONLY, 0, fSize_in);
						MappedByteBuffer mBuf_out = fChan_out.map(FileChannel.MapMode.READ_WRITE, 0, fSize_in);
						resultStr = "��������� ����"+filepath_in.getFileName()+"����������� � �������� mBuf_in\n";
						resultStr += "���� ����������"+filepath_out.getFileName()+"����������� � �������� mBuf_out\n";
						tv.textarea.append(resultStr);
						// ������ �� ������
						resultStr = "������ �� ������...\n";
						tv.textarea.append(resultStr);
						text = "";
						for (int i=0; i<fSize_in; i++){
							text += (char)mBuf_in.get();
						}
						resultStr = "\n������ �������� � ���������� text\n";
						tv.textarea.append(resultStr);
						StringTokenizer txt = new StringTokenizer(text);
						StringBuffer revers;
						text = "";
						resultStr = "���������� �������.";
						tv.textarea.append(resultStr);
						while (txt.hasMoreTokens()){
							revers = new StringBuffer(txt.nextToken("\n"));
							text += revers.reverse().toString()+"\n";
						}
						resultStr = "������ ����������\n������ � �������� �����.";
						tv.textarea.append(resultStr);
						for (int i=0; i<text.length(); i++){
							mBuf_out.put((byte)text.charAt(i));
						}
						resultStr += "\n����������� �������\n";
						resultStr += "n��������� ������� � ���� - "+filepath_out.getFileName()+"\n";
						tv.textarea.append(resultStr);
						mBuf_in.clear();
						mBuf_out.clear();
						fChan_in.close();
						fChan_out.close();
					} catch (InvalidPathException e){
						System.out.println("Path error"+e.getMessage());
						return;
					}
					// ������� ������ FileViewer
					Frame fv = new FileViewer(path_out, File_name_out,0);
					fv.setVisible(true);
				}
			}
		}catch (IOException e) {
			System.err.println(e.getMessage());
		}// �������� finally ??????????????????????
	}
	// �������� �����
	public void createFile(String path, String Name){
		resultStr = "�������� ����� ������� - createFile(String path, String Name)\n";
		tv.textarea.append(resultStr);
		file = new File(path+Name);
		try {
			if (file.exists()) file.delete();
			file.createNewFile();
			resultStr = "���� � ������ - "+file.getName()+" ������\n";
			tv.textarea.append(resultStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// ���������� ����� ���������� ������ �������
	public void randomIntToFile(String path, String Name, int num) {
		resultStr = "���������� ����� ���������� ������ �������\n";
		resultStr += "������� - randomIntToFile(String path, String Name, int num)\n";
		tv.textarea.append(resultStr);
		createFile(path, Name);
		try {
			if (!file.exists()) abort("���� - "+path+Name+" �� ����������");
			if (!file.canWrite()) abort("���� - "+path+Name+" �� �������� ��� ������");
			raf = new RandomAccessFile(file,"rw");
			// ��������� ��������� ����� �����
			resultStr = "��������� ��������� ����� ����� � ������ �� � ����.";
			tv.textarea.append(resultStr);
			while (num > 0) {
				raf.writeInt((int)(Math.random()*100));
				num--;
				resultStr = ".";
				tv.textarea.append(resultStr);
			}
			resultStr = "\n����������� �������.";
			tv.textarea.append(resultStr);
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	// ���������� ����������� ����� (int ��������)
	public void sortRandomIntFile(String path, String name, int vid){
		resultStr = "���������� ����������� ����� (int ��������)\n";
		resultStr += "������� - sortRandomIntFile(String path, String name, int vid)\n";
		tv.textarea.append(resultStr);
		file = new File(path+name);
		text = "";
		try {
			if (!file.exists()) abort("���� - "+path+name+" �� ����������");
			if (!file.canRead()) abort("���� - "+path+name+" �� �������� ��� ������");
			raf = new RandomAccessFile(file,"rw");
			int a[] = new int[(int)raf.length()/4];
			// ������ �� ����� � ������
			text = "���������� ����� ���������� ������ �������:\n";
			resultStr = "���������� ����� ���������� ������ �������:\n";
			tv.textarea.append(resultStr);
			for (int i=0; i<raf.length()/4; i++) {
				a[i] = (int)raf.readInt();
				text += a[i]+" ";
				resultStr = ".";
			}
			raf.close();
			// �������� ������
			//System.out.println("�������� �� ����� �� ���������� "+a.length);
			resultStr = "�������� ������:\n";
			tv.textarea.append(resultStr);
			for (int i=0; i<a.length; i++) {
				//System.out.println("a["+i+"] - "+a[i]);
				resultStr = "a["+i+"] - "+a[i]+"\n";
				tv.textarea.append(resultStr);
			}
			//System.out.println("�������� �� ����� ����� ����������");
			text += "\n�������� �� ����� ����� ����������\n";
			// ���������� ������� � ������ � ����
			resultStr = "\n���������� ������� � ������ � ����.\n";
			tv.textarea.append(resultStr);
			int temp = 0;
			for (int i=0; i<a.length; i++){
				for (int j=1; j<a.length; j++){
					if (a[j-1]>a[j]) {
						temp = a[j-1];
						a[j-1] = a[j];
						a[j] = temp;
						resultStr = ".";
						tv.textarea.append(resultStr);
					}
				}
			}
			resultStr = "\n��������������� ������:\n";
			tv.textarea.append(resultStr);
			for (int i=0; i<a.length; i++) {
				//System.out.println("a["+i+"] - "+a[i]);
				text += a[i]+" ";
				resultStr = "a["+i+"] - "+a[i]+"\n";
				tv.textarea.append(resultStr);
			}
			//System.out.println(text);
			fw = new FileWriter(file);
			fw.write(text);
			fw.close();
			text = "";
			// ������� ������ FileViewer
			Frame f = new FileViewer(file.getParent(),file.getName(),vid);
			// ���������� � ������ �� �������� ���� FileViewer
			/*f.addWindowListener(new WindowAdapter(){
				public void windowClosed(WindowEvent e) {
					System.exit(0);
				}
			});*/
			// �������� ����
			f.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// ������ ��������� �������� �������
	public void replaseSubStr(String path, String name, String path_out, String name_out, String substr, String new_substr, int vid){
		resultStr = "������ ��������� �������� �������\n";
		resultStr += "������� - replaseSubStr(String path, String name, String path_out, String name_out, String substr, String new_substr, int vid):\n";
		tv.textarea.append(resultStr);
		text = "";
		try {
			checkInFile(path, name);
			if (fileOk == true) {
				checkOutFile(path_out, name_out);
				if (fileOk == true) {
					switch (vid) {
					case 0: {
						// ������ - BufferedReader, ������ - PrintWriter
						resultStr = "������� ������ - BufferedReader, ������ - PrintWriter\n";
						tv.textarea.append(resultStr);
						try { 
								// ������;
							br = new BufferedReader(new FileReader(file));
								// ������
							pw = new PrintWriter(new BufferedWriter(new FileWriter(to_file)));
							resultStr = "������ ��� ������ � ������ �������\n";
							resultStr += "������ � ������....\n���������:\n";
							tv.textarea.append(resultStr);
							text = "";
							String repl_str;
							while ((text = br.readLine())!= null){
								repl_str = text.replaceAll(substr, new_substr)+"\n";
								pw.print(repl_str);
								//tv.textarea.append(repl_str);
							}
							text = "";
						} catch (IOException e) {
							e.printStackTrace();
						}
						finally {
							try{
								if (br != null) {
									br.close();
								}
								if (pw != null) {
									pw.close();
								}
							} catch (IOException e) {
								System.err.println("������ �������� ������ �����/������"+e);
							}
						}
						break;
					}
					case 1: {
						// ������ � ������ ����� ����� �����/������
						resultStr = "������� ������ � ������ ����� ����� �����/������ - java.nio\n";
						tv.textarea.append(resultStr);
						int count;
						text = "";
						Path filepath_in = null;
						Path filepath_out = null;
						// �������� ���� � �����
						resultStr = "�������� ���� � �����\n";
						tv.textarea.append(resultStr);
						try {
							filepath_in = Paths.get(file.toString());
							resultStr = "���� � ����� "+filepath_in.getParent()+filepath_in.getFileName()+" �������\n";
							tv.textarea.append(resultStr);
							filepath_out = Paths.get(to_file.toString());
							resultStr = "���� � ����� "+filepath_out.getFileName()+" �������\n";
							tv.textarea.append(resultStr);
						} catch (InvalidPathException e) {
							System.out.println("Path error"+e.getMessage());
						}
						try {
							// �������� ������
								// ����� ��� ������
							SeekableByteChannel fChan_in = Files.newByteChannel(filepath_in);
							resultStr = "����� ��� ������ "+fChan_in.toString()+" �������\n";
							tv.textarea.append(resultStr);
								// ����� ��� ������
							SeekableByteChannel fChan_out = Files.newByteChannel(filepath_out, 
																				StandardOpenOption.WRITE,
																				StandardOpenOption.CREATE);
							resultStr = "����� ��� ������ "+fChan_out+" �������\n";
							tv.textarea.append(resultStr);
							// ����������� ������
								// ������
							ByteBuffer buf_in = ByteBuffer.allocate(128);
							resultStr = "����� ��� ������, �������� 128 ���� "+buf_in+" ��������������\n";
							tv.textarea.append(resultStr);
								// ������
							ByteBuffer buf_out = ByteBuffer.allocate(128);
							resultStr = "����� ��� ������, �������� 128 ���� "+buf_out+" ��������������\n";
							tv.textarea.append(resultStr);
							// ������ �� ������ � ������
							text = "";
							do {
								count = fChan_in.read(buf_in);
								if (count != -1){
									// ������� � ������ �������
									buf_in.rewind();
									for (int i=0; i<count; i++){
										text += (char)buf_in.get();
									}
									text = text.replaceAll(substr, new_substr)+"\n";
									buf_out.rewind();
									buf_out.clear();
									buf_out.limit(count);
									for (int i=0; i<count; i++){
										buf_out.put((byte)text.charAt(i));
									}
									buf_out.rewind();
									buf_out.limit(count);
									fChan_out.write(buf_out);
									buf_in.clear();
									buf_out.clear();
									text="";
								}
							} while (count != -1);
							fChan_in.close();
							fChan_out.close();
							text = "";
						} catch (IllegalArgumentException e) {
							fail("������ � ���������� ������ newByteChannel - "+filepath_in.toString()+" ��� "+ filepath_out.toString());
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
					case 2: {
						// ������ � ������ ����� ������������� ����� � ��������
						Path filepath_in = null;
						Path filepath_out = null;
						// �������� ���� � ������ 
						try {
							filepath_in = Paths.get(file.toString());
							filepath_out = Paths.get(to_file.toString());
						} catch (InvalidPathException e) {
							System.out.println("Path error"+e.getMessage());
						}
						// ������� ������
						FileChannel fChan_in = (FileChannel)Files.newByteChannel(filepath_in, 
																					StandardOpenOption.READ);
						FileChannel fChan_out = (FileChannel)Files.newByteChannel(filepath_out, 
																					StandardOpenOption.READ,
																					StandardOpenOption.WRITE,
																					StandardOpenOption.CREATE);
						// �������� ������ �����
						long file_size = fChan_in.size();
						// ������������ ���� � ������� ������
						try {
							MappedByteBuffer mbuf_in = fChan_in.map(FileChannel.MapMode.READ_ONLY, 0, file_size);
							// ������ �� �����
							text = "";
							for (int i=0; i<file_size; i++){
								text += (char)mbuf_in.get();
							}
							text = text.replaceAll(substr, new_substr);
							// ������ � ����
							// ������������ ���� � ������� ������ 
							MappedByteBuffer mbuf_out = fChan_out.map(FileChannel.MapMode.READ_WRITE, 0, (long)text.length());
							for (int i=0; i<text.length(); i++){
								mbuf_out.put((byte)text.charAt(i));
							}
							mbuf_in.clear();
							mbuf_out.clear();
							fChan_in.close();
							fChan_out.close();
							text = ""; 
						} catch (BufferOverflowException e) {
							System.err.println("������������ �������"+e);
						} finally {
							if (fChan_in != null) fChan_in.close();
							if (fChan_out != null) fChan_out.close();
						}
						break;
					}
					}
				}
			}
			Frame f = new FileViewer(path_out, name_out, vid);
			f.setVisible(true);
		} catch (FileNotFoundException e) {
			System.err.println("�� ������ ����"+e);
		} catch (IOException e){
			e.printStackTrace();
		} 
	}
	// ������ � ������ �����, ������ 2-� ��������, �������� �������� �� ���������
	public void charToUpperCase(String path, String name, String path_out, String name_out, int vid){
		//System.out.println("������� ����� charToUpperCase()");
		//resultStr = "";
		resultStr = "������ � ������ �����, ������ 2-� ��������, �������� �������� �� ���������\n";
		resultStr += "- ������� ����� charToUpperCase()\n";
		tv.textarea.append(resultStr);
		text = "";
		try {
			checkInFile(path, name);
			if (fileOk = true) {
				checkOutFile(path_out, name_out);
				if (fileOk = true) {
					switch (vid){
					case 0: {
						// ������ - BufferedReader, ������ - PrintWriter
						resultStr = "������ - BufferedReader, ������ - PrintWriter\n";
						tv.textarea.append(resultStr);
						try {
							// ������
							br = new BufferedReader(new FileReader(file));
							resultStr = "- ���� "+file.getName()+" ������ ��� ������\n";
							tv.textarea.append(resultStr);
							// ������
							pw = new PrintWriter(new BufferedWriter(new FileWriter(to_file)));
							resultStr = "- ���� "+to_file.getName()+" ������ ��� ������\n���������� ������� �����:\n";
							tv.textarea.append(resultStr);
							while ((text = br.readLine()) != null) {
								sToken = new StringTokenizer(text);
								text = "";
								while (sToken.hasMoreTokens()){
									text = sToken.nextToken();
									if (text.length()>3){
										pw.print(text.toLowerCase()+" ");
									} else {
										if (text.length()<3) {
											pw.print(text+" ");
										} else {
											if ((text.length() == 3)&(text.charAt(2)==';')) {
												pw.print(text+" ");
											} else {
												pw.print(text.toLowerCase()+" ");
											}
										}
									}
								}
								pw.print("\n");
							}
							text = "";
							br.close();
							pw.close();
							resultStr = "- ������ � ���� "+to_file.getName()+" ���������\n";
							tv.textarea.append(resultStr);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try{
								if (br != null) {
									br.close();
								}
								if (pw != null) {
									pw.close();
								}
							} catch (IOException e) {
								System.err.println("������ �������� ������ �����/������"+e);
							}
						}
						break;
					}
					case 1: {
						resultStr = "������ � ������ ����� ����� �����/������\n";
						tv.textarea.append(resultStr);
						int count = 0;
						Path filepath_in = null;
						Path filepath_out = null;
						text = "";
						// ������� �������� ���� � �����
						resultStr = "�������� ���� � ������:\n";
						tv.textarea.append(resultStr);
						try {
							filepath_in = Paths.get(file.toString());
							filepath_out = Paths.get(to_file.toString());
						} catch (InvalidPathException e) {
							message = "Path error"+e.getMessage();
							JOptionPane.showMessageDialog(null, message,"���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
						}
						resultStr = "���� � ������ ������� - ��. - "+filepath_in.toString()+"\n";
						resultStr += "���. - "+filepath_out.toString()+"\n";
						tv.textarea.append(resultStr);
						try {
							// ������� ������� ������ �����
							resultStr = "��������� ������ �����/������:\n";
							tv.textarea.append(resultStr);
							SeekableByteChannel fChan_in = Files.newByteChannel(filepath_in);
							SeekableByteChannel fChan_out = Files.newByteChannel(filepath_out, StandardOpenOption.WRITE,
																	StandardOpenOption.CREATE);
							resultStr = "������ ��. - "+fChan_in.toString()+" �\n";
							resultStr += "���. - "+fChan_out.toString()+" �������\n";
							tv.textarea.append(resultStr);
							// ����������� ����� ��� ������
							ByteBuffer buf_in = ByteBuffer.allocate(128);
							resultStr = "����� ��� ������ "+buf_in+" ������\n";
							tv.textarea.append(resultStr);
							// ������ 
							text = "";
							temp = "";
							do {
								count = fChan_in.read(buf_in);
								if (count != -1){
									buf_in.rewind();
									for (int i=0; i<count; i++){
										text += (char)buf_in.get(i); 
									}	
								}
							} while (count != -1);
							resultStr = "�������� ���� �������� � text\n";
							tv.textarea.append(resultStr);
							mas_str();
							resultStr = "������ �����\n";
							tv.textarea.append(resultStr);
							for (int i=0; i<str.length; i++){
								sToken = new StringTokenizer(str[i]);
								while (sToken.hasMoreTokens()){
									text = sToken.nextToken();
									if (text.length()>3){
										temp += text.toLowerCase()+" ";
									} else {
										if (text.length()<3) {
											temp += text+" ";
										} else {
											if ((text.length() == 3)&(text.charAt(2)==';')) {
													temp += text+" ";
											} else {
												temp += text.toLowerCase()+" ";
											}
										}
									}
								}
								temp += "\n";
							}
							// ����������� ����� ��� ������
							ByteBuffer buf_out = ByteBuffer.allocate(temp.length());
							resultStr = "����� ��� ������ "+buf_out+" ��������������\n";
							tv.textarea.append(resultStr);
							for (int j=0; j<temp.length(); j++){
								buf_out.put((byte)temp.charAt(j));
							}
							resultStr = "������� ���� ������� � ����� ��� ������\n";
							tv.textarea.append(resultStr);
							buf_out.rewind();
							buf_out.limit(temp.length());
							fChan_out.write(buf_out);
							resultStr = "������ � �������� ���� ������������\n";
							tv.textarea.append(resultStr);
							buf_in.clear();
							buf_out.clear();
							text = "";
						} catch (IllegalArgumentException e) {
							fail("������ � ���������� ������ newByteChannel - "+filepath_in.toString()+" ��� "+ filepath_out.toString());
						}
						break;
					}
					case 2: {
						// ������/������ ��������� ������������� ����� � ������
						// ������� �������� ���� � �����
						resultStr = "������/������ ��������� ������������� ����� � ������\n";
						resultStr += "������� �������� ���� � �����:\n";
						tv.textarea.append(resultStr);
						Path filepath_in = null;
						Path filepath_out = null;
						try {
							filepath_in = Paths.get(file.toString());
							filepath_out = Paths.get(to_file.toString());
						} catch (InvalidPathException e) {
							message = "Path error"+e.getMessage();
							JOptionPane.showMessageDialog(null, message,"���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
						}
						// ���� � ������ �������
						// ������� ������
						resultStr = "���� � ������:"+filepath_in.toString()+"\n� "+filepath_out.toString()+" �������\n";
						resultStr += "������� ������:\n";
						tv.textarea.append(resultStr);
						FileChannel fChan_in = (FileChannel)Files.newByteChannel(filepath_in, StandardOpenOption.READ);
						FileChannel fChan_out = (FileChannel)Files.newByteChannel(filepath_out, StandardOpenOption.WRITE,
																				StandardOpenOption.CREATE,
																				StandardOpenOption.READ);
						// �������� ������ ��������� �����						
						resultStr = "������ �������: "+fChan_in.toString()+"\n� "+fChan_out.toString()+"\n";
						resultStr += "�������� ������ ��������� ����� - ";
						tv.textarea.append(resultStr);
						long fSize = fChan_in.size();
						// ������������ ����� � ��������
						resultStr = fSize+"\n������������ ����� � ���������:\n";
						tv.textarea.append(resultStr);
						try {
							MappedByteBuffer mBuf_in = fChan_in.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
							text = "";
							temp = "";
							// ������
							resultStr = "1. ������\n";
							tv.textarea.append(resultStr);
							for (int i=0; i<(int)fSize; i++){
								text += (char)mBuf_in.get();
							}
							resultStr = " - �������� ���� �������� � text\n";
							tv.textarea.append(resultStr);
							mas_str();
							resultStr = "2. ������ �����\n";
							tv.textarea.append(resultStr);
							for (int i=0; i<str.length; i++){
								sToken = new StringTokenizer(str[i]);
								while (sToken.hasMoreTokens()){
									text = sToken.nextToken();
									if (text.length()>3){
										temp += text.toLowerCase()+" ";
									} else {
										if (text.length()<3) {
											temp += text+" ";
										} else {
											if ((text.length() == 3)&(text.charAt(2)==';')) {
													temp += text+" ";
											} else {
												temp += text.toLowerCase()+" ";
											}
										}
									}
								}
								temp += "\n";
							}
							// ����������� ����� ��� ������
							resultStr = "3.����������� ����� ��� ������ - ";
							tv.textarea.append(resultStr);
							MappedByteBuffer mBuf_out = fChan_out.map(FileChannel.MapMode.READ_WRITE, 0, (long)temp.length());
							// ����� � ������
							resultStr = mBuf_out.toString()+"\n4.����� � ������\n";
							tv.textarea.append(resultStr);
							for (int i=0; i<temp.length(); i++){
								mBuf_out.put((byte)temp.charAt(i));
							}
							mBuf_in.clear();
							mBuf_out.clear();
							fChan_in.close();
							fChan_out.close();
							text = "";
							temp = "";
						} catch (NonReadableChannelException e) {
							message = "������ �� ������ ��� ������ "+e.getMessage();
							JOptionPane.showMessageDialog(null, message,"���������� - NonReadableChannelException", JOptionPane.ERROR_MESSAGE);
						} catch (NonWritableChannelException e) {
							message = "������ �� ������ ��� ������ "+e.getMessage();
							JOptionPane.showMessageDialog(null, message,"���������� - NonWritableChannelException", JOptionPane.ERROR_MESSAGE);
						} catch (IllegalArgumentException e) {
							message = "��������� ��������� ������ map "+e.getMessage();
							JOptionPane.showMessageDialog(null, message,"���������� - IllegalArgumentException", JOptionPane.ERROR_MESSAGE);
						} catch (BufferOverflowException e) {
							message = "������������ ������� "+e.getMessage();
							JOptionPane.showMessageDialog(null, message,"���������� - BufferOverflowException", JOptionPane.ERROR_MESSAGE);							
						} finally {
							if (fChan_in != null) fChan_in.close();
							if (fChan_out != null) fChan_out.close();
						}
						break;
					}
					}
				}
			}
			Frame f = new FileViewer(path_out, name_out, vid);
			f.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// ��������
	public void student(String path, String Name, int sr){
		resultStr = "������� ����� student()\n";
		tv.textarea.append(resultStr);
		resultStr = "������ ������ student()\n";
		tv.textarea.append(resultStr);
		int size = 0;
		try {
			resultStr = "������� �������� ������ - FileInputStream(path+Name)\n";
			tv.textarea.append(resultStr);
			FileInputStream f = new FileInputStream(path+Name);
			size = f.available();
			resultStr = "����� ������, ������ ����� -"+size+"\n";
			tv.textarea.append(resultStr);
			f.close();
		} catch (IOException e) {
			message = "I/O Error: "+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
		try {
			resultStr = "������� �������� ������ ������/������ RandomAccessFile()\n";
			tv.textarea.append(resultStr);
			raf = new RandomAccessFile(path+Name,"rw");
			resultStr = "����� ������\n������� �������� ������ � ������ ��� ������ RandomAccessFile()\n";
			tv.textarea.append(resultStr);
			MappedByteBuffer in_out = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);
			resultStr = "����� � ����� ��� ������ RandomAccessFile() - �������\n";
			tv.textarea.append(resultStr);
			text = "";
			int count = 0;
			double sum = 0, sredn = 0;
			char b;
			for (int j=0; j<size; j++) {
				b = (char)in_out.get(j);
				in_out.position(j+1);
				text += b;
				if (b == '\n') {
					// ������������� � StringTokenizer ��� StringBuilder
					Scanner scan = new Scanner(text);
					resultStr = "����� Scanner(text), ��� ��������� ������ �� ����� - ������\n";
					tv.textarea.append(resultStr);
					scan.useDelimiter(";\\s*");
					resultStr = "���� ��� ���������� �������� ���� �������\n";
					tv.textarea.append(resultStr);
					while (scan.hasNext()){
						if (scan.hasNextInt()) {
							sum +=scan.nextInt();
							count++;
						} else scan.next();
					}
					scan.close();
					sredn = sum/count;
					resultStr = "������� ��� - "+sredn+"\n";
					tv.textarea.append(resultStr);
					if (sredn >= sr) {
						// ������ � ���� ����������
						resultStr = "������ � ���� � ������ ��������\n";
						tv.textarea.append(resultStr);
						in_out.position(in_out.position()-text.length());
						text = text.toLowerCase();
						for (int i=0; i<text.length(); i++){
							in_out.put((byte)text.charAt(i));
						}
					}
					text = "";
					count = 0;
					sum = 0;
					resultStr = "��������� �����\n";
					tv.textarea.append(resultStr);
				}
			}
			in_out.clear();
			raf.close();
			Frame f = new FileViewer(path, Name, 0);
			f.setVisible(true);
		}catch (FileNotFoundException e) {
			message = "���� �� ������: "+e;
			JOptionPane.showMessageDialog(null, message, "���������� - FileNotFoundException", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e){
			message = "�������� ������������ RandomAccessFile() �� ������������ ������ �� \"r\", \"rw\", \"rws\", or \"rwd\": "+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IllegalArgumentException", JOptionPane.ERROR_MESSAGE);
		}catch (IOException e) {
			message = "I/O Error: "+e;
			JOptionPane.showMessageDialog(null, message, "���������� - IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	// �������� 2
	public void student2(String path, String Name, int sr){
		resultStr = "������� ����� student2()\n";
		tv.textarea.append(resultStr);
		Path in_out = null;
		try {
			in_out = Paths.get(path+Name);
		} catch (InvalidPathException e){
			message = "Path error"+e.getMessage();
			JOptionPane.showMessageDialog(null, message,"���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
		}
		try {
			FileChannel fChan = (FileChannel)Files.newByteChannel(in_out, StandardOpenOption.READ,
																	StandardOpenOption.WRITE);
			int size = (int)fChan.size();
			MappedByteBuffer mBuf = fChan.map(FileChannel.MapMode.READ_WRITE, 0, size);
			text = "";
			char s;
			double sum = 0, sredn = 0;
			int count = 0;
			for (int i=0; i<(int)size; i++){
				s = (char)mBuf.get();
				text += s;
				if (s == '\n') {
					// ������������� � StringTokenizer ��� StringBuilder
					Scanner scan = new Scanner(text);
					resultStr = "����� Scanner(text), ��� ��������� ������ �� ����� - ������\n";
					tv.textarea.append(resultStr);
					scan.useDelimiter(";\\s*");
					resultStr = "���� ��� ���������� �������� ���� �������\n";
					tv.textarea.append(resultStr);
					while (scan.hasNext()){
						if (scan.hasNextInt()) {
							sum +=scan.nextInt();
							count++;
						} else scan.next();
					}
					scan.close();
					sredn = sum/count;
					resultStr = "������� ��� - "+sredn+"\n";
					tv.textarea.append(resultStr);
					if (sredn >= sr) {
						// ������ � ���� ����������
						resultStr = "������ � ���� � ������ ��������\n";
						tv.textarea.append(resultStr);
						mBuf.position(mBuf.position()-text.length());
						text = text.toLowerCase();
						for (int j=0; j<text.length(); j++){
							mBuf.put((byte)text.charAt(j));
						}
					}
					text = "";
					count = 0;
					sum = 0;
					resultStr = "��������� �����\n";
					tv.textarea.append(resultStr);
				}
			}
			mBuf.clear();
			fChan.close();
			Frame f = new FileViewer(path, Name, 0);
			f.setVisible(true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//������ ������ ������
	public void unalisLexem(String path_in, String name_in, String path_out, String name_out, String type, String delimiter){
		resultStr = "������� ����� unalisLexem()\n";
		tv.textarea.append(resultStr);
		// ������� ������ ����� ������
		String[] mass_type = {"String", "int", "byte", "double", "boolean", "float", "long"};
		// ��������� �����
		try {
			checkInFile(path_in, name_in);
			if (fileOk == true) {
				checkOutFile(path_out, name_out);
				if (fileOk == true){
					// �������� ���� � ������
					Path fpath_in = null;
					Path fpath_out = null;
					try {
						fpath_in = Paths.get(path_in+name_in);
						fpath_out = Paths.get(path_out+name_out);
						resultStr = "���� � ������ - "+fpath_in+" � "+fpath_out+" - �������\n";
						tv.textarea.append(resultStr);
					} catch (InvalidPathException e){
						message = "Path error"+e.getMessage();
						JOptionPane.showMessageDialog(null, message,"���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
					}
					// �������� ������ �����/������
					try {
						FileChannel fChan_in = (FileChannel)Files.newByteChannel(fpath_in, StandardOpenOption.READ);
						FileChannel fChan_out = (FileChannel)Files.newByteChannel(fpath_out, StandardOpenOption.READ,
																								StandardOpenOption.WRITE,
																									StandardOpenOption.CREATE);
						resultStr = "������ - "+fChan_in.toString()+" � "+fChan_out.toString()+" - �������\n";
						tv.textarea.append(resultStr);
						// ������������� ����� ������
						long fSize = fChan_in.size();
						MappedByteBuffer mBuf_in = fChan_in.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
						resultStr = "����� ��� ������ ����������� � - "+mBuf_in.toString()+" �������\n";
						// ������������� ����� ������
						text = "you input type - "+type+"\n";
						MappedByteBuffer mBuf_out = fChan_out.map(FileChannel.MapMode.READ_WRITE, 0, fSize+text.length());
						resultStr = "����� ��� ������ � ������� - "+mBuf_out+" �����������\n";
						// ������ � ����
						for (int j=0; j<text.length(); j++){
							mBuf_out.put((byte)text.charAt(j));
						}
						resultStr += "������ �� ������...\n";
						tv.textarea.append(resultStr);
						// ������ ���. ����
						char s;
						text = "";
						for (int i=0; i<(int)fSize; i++) {
							s = (char)mBuf_in.get();
							text += s;
							if (s == '\n') {
								resultStr = "������ ������ ���������, ������� ����� Scanner...\n";
								tv.textarea.append(resultStr);
								Scanner scan = new Scanner(text);
								resultStr = "����� Scanner ������\n";
								tv.textarea.append(resultStr);
								scan.useDelimiter(delimiter);
								text = "";
								int int_type = 0;
								for (int j=0; j<mass_type.length; j++){
									if (mass_type[j].equalsIgnoreCase(type)){
										int_type = j;
									}
								}
								resultStr = "� ������ ������ ��� ����� ��� ������ ��� ������ - "+type+"\n";
								resultStr += "���������� � ������� ����� - "+int_type+"\n";
								tv.textarea.append(resultStr);
								switch (int_type){
								case 0: { //String
									while (scan.hasNext()){
										if (scan.hasNextInt()) scan.nextInt();
											else if (scan.hasNextByte()) scan.nextByte();
												else if (scan.hasNextDouble()) scan.nextDouble();
													else if (scan.hasNextBoolean()) scan.nextBoolean();
														else if (scan.hasNextFloat()) scan.nextFloat();
															else if (scan.hasNextLong()) scan.nextLong();
															else text += scan.next() + ", ";
												
										
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								case 1: { //int
									while (scan.hasNext()){
										if (scan.hasNextInt()) {
											text +=scan.nextInt()+", ";
										} else scan.next();
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								case 2: { //byte
									while (scan.hasNext()){
										if (scan.hasNextByte()) {
											text +=scan.nextByte()+ ", ";
										} else scan.next();
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								case 3: { //double
									while (scan.hasNext()){
										if (scan.hasNextDouble()) {
											text += scan.nextDouble() + ", ";
										} else scan.next();
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								case 4: { //boolean
									while (scan.hasNext()){
										if (scan.hasNextBoolean()){
											text += scan.nextBoolean() + ", ";
										} else scan.next();
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								case 5: { //float
									while (scan.hasNext()){
										if (scan.hasNextFloat()){
											text += scan.nextFloat() + ", ";
										} else scan.next();
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								case 6: { // long
									while(scan.hasNext()){
										if (scan.hasNextLong()){
											text += scan.nextLong() + ", ";
										} else scan.next();
									}
									resultStr = "��������� - "+text+"\n";
									tv.textarea.append(resultStr);
									break;
								}
								}
								resultStr = "������ � �����...\n";
								tv.textarea.append(resultStr);
								// ������ � ����
								for (int j=0; j<text.length(); j++){
									mBuf_out.put((byte)text.charAt(j));
								}
								resultStr = "������ �������\n";
								tv.textarea.append(resultStr);
								text = "";
							}
						}
						
						mBuf_in.clear();
						mBuf_out.clear();
						fChan_in.close();
						fChan_out.close();
						Frame f = new FileViewer(path_out, name_out, 0);
						f.setVisible(true);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		} 
	}
	//==================
	// �������� ���� ��������� �������
	public void deleteWords(String path, String name, int min, int max){
		resultStr = "������� ����� deleteWords(String path, String name, int min, int max)\n";
		tv.textarea.append(resultStr);
		// �������� ������������� �����
		try {
			checkInFile(path, name);
			if (fileOk == true) {
				// �������� ���� � �����
				Path filePath = null;
				try {
					filePath = Paths.get(path+name);
					resultStr = "���� � ����� ������� - "+filePath.toString()+"\n";
					tv.textarea.append(resultStr);
				} catch (InvalidPathException e) {
					message = "Path error"+e.getMessage();
					JOptionPane.showMessageDialog(null, message,"���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
				}
				try {
					// �������� ����� ��� ������ � ������
					FileChannel fChan = (FileChannel)Files.newByteChannel(filePath, StandardOpenOption.READ,
																					 StandardOpenOption.WRITE);
					resultStr = "����� ��� ������/������ - "+fChan.toString()+" �������\n";
					tv.textarea.append(resultStr);
					// �������� ������ �����
					long fSize = fChan.size();
					resultStr = "������ ����� - "+fSize+"\n";
					tv.textarea.append(resultStr);
					// ����������� ����� � ������� ������ � ������
					MappedByteBuffer mBuf = fChan.map(FileChannel.MapMode.READ_WRITE, 0, fSize);
					resultStr = "����� ��� ������/������ - "+fChan+" ����������� \n� �������� - "+mBuf.toString()+" ������/������ ��/� ����(�)\n";
					tv.textarea.append(resultStr);
					// ������ �� ������
					text = "";
					char s;
					for (int i=0; i<(int)fSize; i++){
						s = (char)mBuf.get();
						text += s;
						if (s == '\n'){
							// ������ ��������� ������
							resultStr = "������ ��������� ������...\n";
							tv.textarea.append(resultStr);
							temp = text;
							String[] mas_Str = text.split("\\W+");
							int count = 0;
							for (int j=0; j<mas_Str.length; j++){
								resultStr = "����� - "+mas_Str[j]+"\n";
								tv.textarea.append(resultStr);
								if ((mas_Str[j].length()>(min-1))&(mas_Str[j].length()<(max+1))) count++;
							}
							text = "";
							resultStr = "���-�� ���� ��� �������� (count) - "+count+"\n";
							tv.textarea.append(resultStr);
							int count_del = 0;
							if (count%2==0) count_del = count;
							else count_del = count -1;
							resultStr = "���-�� ���� ��� �������� (count_del) - "+count_del+"\n";
							tv.textarea.append(resultStr);
							for (int j=0; j<mas_Str.length; j++){
								if ((mas_Str[j].length()>=min)&(mas_Str[j].length()<=max)&(count_del>0)) {
										count_del--;
										System.out.println("text - "+text);
								} else  text += mas_Str[j]+" ";
							}
							resultStr = "���������� ��������������� ����� ���������, ��� ���������� ��������� ������ � �����...\n";
							tv.textarea.append(resultStr);
							int len = temp.length()-text.length()-1;
							resultStr = "������� ���� ������ � ����� ����� - "+len+"\n";
							tv.textarea.append(resultStr);
							for (int j=0; j<len; j++){
								text += " ";
							}
							// ������ � ����
							resultStr = "������ � ����...\n";
							tv.textarea.append(resultStr);
							text += "\n";
							resultStr = "������� ������� ������� mBuf.position() - "+mBuf.position()+"\n";
							tv.textarea.append(resultStr);
							resultStr = "����� ������� ������� mBuf.position() - "+(mBuf.position()-temp.length())+"\n";
							tv.textarea.append(resultStr);
							mBuf.position(mBuf.position()-temp.length());
							for (int j=0; j<text.length(); j++){
								mBuf.put((byte)text.charAt(j));
							}
							text = "";
						}
					}
					Frame f = new FileViewer(path, name, 0);
					f.setVisible(true);
				} catch (BufferUnderflowException e) {
					e.printStackTrace();
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NonReadableChannelException e) {
					e.printStackTrace();
				} catch (NonWritableChannelException e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// �������� ������ �������� � ���������
	public void deleteProbelTab(String path, String name) {
		resultStr = "������� ����� deleteProbelTab(String path, String name)\n";
		tv.textarea.append(resultStr);
		// �������� ������������� �����
		try {
			checkInFile(path, name);
			if (fileOk == true) {
				// �������� ���� � �����
				Path fPath = null;
				try {
					fPath = Paths.get(path+name);
					resultStr = "���� � ����� ������� - "+fPath.toString()+"\n";
					tv.textarea.append(resultStr);
				} catch (InvalidPathException e) {
					message = "Path error"+e.getMessage();
					JOptionPane.showMessageDialog(null, message,"���������� - InvalidPathException", JOptionPane.ERROR_MESSAGE);
				}
				// ��������� ������� ����� ����� PushbackInputStream
				// ������� ����� �����/������ � ������������ ��� � �������� �������
					// ������ ������
				int size = 0;
				resultStr = "��� ��������� ������� ������ ������� ����� - FileInputStream(path+name)\n";
				tv.textarea.append(resultStr);				
				FileInputStream f = new FileInputStream(path+name);
				size = f.available();
				resultStr = "����� ������, ������ ����� -"+size+"\n";
				tv.textarea.append(resultStr);
				//---
				FileChannel fChan = (FileChannel)Files.newByteChannel(fPath, StandardOpenOption.READ,
																StandardOpenOption.WRITE);
				MappedByteBuffer mBuf = fChan.map(FileChannel.MapMode.READ_WRITE, 0, size);
				//---
				//f.close();
				FileReader in = new FileReader(path+name);
				
				// ������� ����� ���������� �����������
				resultStr = "������� �������� ������ ������/������ PushbackInputStream()\n";
				tv.textarea.append(resultStr);
				//PushbackInputStream pbis = new PushbackInputStream(mBuf);
				resultStr = "����� ������\n������� �������� ������ � ������\n";
				tv.textarea.append(resultStr);
				
				
				text = "";
				int count = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//==================
	// === ����������� ���������� ���������� �������
	// ������� � ���������� ����, ���������� TextArea,
	// � ������� ������������� ���������� �������������� ��������� ������
	public class TextView extends Frame implements ActionListener{
		private static final long serialVersionUID = 5834908167777592367L;
		String Directory, Filename; // ��������� ������������ � FileViewer
		int Vertion;
		TextArea textarea; // �������, � ������� ����� ����������� ���������� �����
		// === ��������������� �����������: ���� ����������� ������
		public TextView() {
			this(null, null, null, 0);
		}
		// === ��������� �����������: ������� ������ FileViewer ��� ����������� 
		// === ��������� ����� �� ��������� ��������
		public TextView(String resultText, String directory, String filename, int vertion){
			super(); // ��������� �����
			// �� ���������� ������������ ���� ������������
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					dispose();
				}
			});
			// === ��������� ������� TextArea ��� ����������� � ��� ����������� �����
			textarea = new TextArea("", 24, 80);
			textarea.setFont(new Font("MonoSpaced",Font.ITALIC,12));
			textarea.setEditable(false);
			this.add("Center", textarea);
			// �������� ������ ������ ��� ���� ������
			Panel p = new Panel();
			p.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
			this.add(p,"South");
			// �������� ���� ������, ����������� ��������� �� �������
			Font font = new Font("SansSerif", Font.BOLD, 14);
			Button openResfile = new Button("Open result file");
			Button close = new Button("Close");
			openResfile.addActionListener(this);
			openResfile.setActionCommand("open");
			openResfile.setFont(font);
			close.addActionListener(this);
			close.setActionCommand("close");
			close.setFont(font);
			p.add(openResfile);
			p.add(close);
			this.pack();
			// ����������� �������������� ������ � textarea
			textarea.setText("");
			textarea.append(resultText);
			Directory = directory;
			Filename = filename;
			Vertion = vertion;
		}
		// === ��������� ������� ������
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			if (cmd.equals("open")) { // ���� ������ ������ Open
				// ���������� ���� � ���������� ����� ���������� ����������
				// ������ 
				Frame fv = new FileViewer(Directory, Filename, Vertion);
				fv.setVisible(true);
			} else if (cmd.equals("close"))	// ���� ������ Close 
				this.dispose(); 			// ���� �����������
		}
	}
	// ==================
	// === ����������� ������
	// ������� � ���������� ����, ���������� TextArea,
	// � ������� ������������� ���������� ���������� �����
	public class FileViewer extends Frame implements ActionListener{
		private static final long serialVersionUID = 5834908167777592367L;
		String Directory; // �������, ����������� �� ��������� fileDialog
		TextArea textareaFV; // �������, � ������� ����� ����������� ���������� �����
		// === ��������������� �����������: ���� ����������� ������
		public FileViewer() {
			this(null, null, 0);
		}
		// === ��������������� �����������: ����������� ���� �� �������� ��������
		public FileViewer(String FileName){
			this(null, FileName, 0);
		}
		// === ��������� �����������: ������� ������ FileViewer ��� ����������� 
		// === ��������� ����� �� ��������� ��������
		public FileViewer(String directory, String filename, int vertion){
			super(); // ��������� �����
			// �� ���������� ������������ ���� ������������
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					dispose();
				}
			});
			// === ��������� ������� TextArea ��� ����������� � ��� ����������� �����
			textareaFV = new TextArea("", 24, 80);
			textareaFV.setFont(new Font("MonoSpaced",Font.PLAIN,12));
			textareaFV.setEditable(false);
			this.add("Center", textareaFV);
			// �������� ������ ������ ��� ���� ������
			Panel p = new Panel();
			p.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
			this.add(p,"South");
			// �������� ���� ������, ����������� ��������� �� �������
			Font font = new Font("SansSerif", Font.BOLD, 14);
			Button openfile = new Button("Open File");
			Button close = new Button("Close");
			openfile.addActionListener(this);
			openfile.setActionCommand("open");
			openfile.setFont(font);
			close.addActionListener(this);
			close.setActionCommand("close");
			close.setFont(font);
			p.add(openfile);
			p.add(close);
			this.pack();
			// ���������� ������� �� ��������� FileName ���, ���� ����������,
			// �������� �������� (dir)
			if (directory == null) {
				File f;
				if ((filename != null)&&(f = new File(filename)).isAbsolute()) {
					directory = f.getParent();
					filename = f.getName();
				} else {
					directory = System.getProperty("user.dir");
				}
			}
			this.Directory = directory; // ������� ������������ ��� ���� FileDialog
			setFile(directory, filename, vertion);
		}
		// === ��������� � ���������� �������� ���� �� ��������� ��������
		public void setFile(String directory, String filename, int vertion){
			if ((filename == null)||(filename.length() == 0)) return;
			// ������� ���������� �����:
			// 0 - ��������� java.io ����� FileReader
			// 1 - ��������� java.nio ����� �����
			// 2 - ��������� java.nio ����� ������������� � �������
			textareaFV.setText(""); // ������� textarea
			switch (vertion){
				case 0: {
					InputOutput.fr = null;
					// ������� � ���������� ���������� �����. �.�. �������� 
					// �����, ��������� ����� FileReader ������ FileInputStream
					try {
						InputOutput.file = new File(directory, filename); // ��������� ������ File
						InputOutput.fr = new FileReader(InputOutput.file);// � ���������� ����� ��� ��� ������ 
						char[] buffer = new char[4096]; // 4k �������� �� ���� �����
						int len; // ����� �������� ��������� �� ���� ���
						
						while ((len = InputOutput.fr.read(buffer))!=-1){ // ���������� ������ ��������
							String s = new String(buffer, 0, len); 	// �������������� � ������
							textareaFV.append(s);						// � ����������� �����
						}
						this.setTitle("FileViewer: "+filename); // ��������� ��������� ����
						textareaFV.setCaretPosition(0); // ������� � ������ �����
					} catch (IOException e){ // ���������� ��������� ���� ���-�� �� ���
						textareaFV.setText(e.getClass().getName()+": "+e.getMessage());
						this.setTitle("FileViewer: "+filename+": ���������� �����/������");
					}
					finally {
						try {
							if (InputOutput.fr!=null) InputOutput.fr.close();
						} catch (IOException e) {
							textareaFV.setText(e.getClass().getName()+": "+e.getMessage());
							this.setTitle("FileViewer: "+filename+": ������ �������� ������ �����");
						}
					}
					break;
				}
				case 1: {
					// ������ ��������� java.nio ����� �����
					String s = ChannelRead(directory, filename); // ���������� ����� � ������ ����� �����
					textareaFV.append(s); 						 // � ����������� ������
					this.setTitle("FileViewer: "+filename); // ��������� ��������� ����
					textareaFV.setCaretPosition(0); // ������� � ������ �����
					break;
				}
				case 2: {
					// ������ ��������� java.nio ����� ������������� � �������
					String s = ChannelBufferRead(directory, filename);// ���������� ����� � ������ ����� ������������ � �������
					textareaFV.append(s); 						 	  // � ����������� ������
					this.setTitle("FileViewer: "+filename); // ��������� ��������� ����
					textareaFV.setCaretPosition(0); // ������� � ������ �����
					break;
				}
			}
		}
		// === ��������� ������� ������
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			if (cmd.equals("open")) { // ���� ������ ������ Open
				// ������� ���������� ����, ������������ ������ ����� ����,
				// ������� ������� ����������
				FileDialog f = new FileDialog(this, "Open File", FileDialog.LOAD);
				f.setDirectory(Directory); // ������������� ������� �� ���������
				// ������������ ���������� ���� � ��������� ����� ������������
				f.setVisible(true);
				Directory = f.getDirectory(); 	// ���������� ����������� �� 
												// ��������� �������
				setFile(Directory, f.getFile(), 0); // ����������� � ������������ 
												 // ��������� ����
				f.dispose(); // �������� ����������� ����
			} else if (cmd.equals("close"))	// ���� ������ Close 
				this.dispose(); 			// ���� �����������
		}
	}
	// ==================
	// ���������� �������
	public static void main(String[] args) {
		InputOutput potok = new InputOutput();
		potok.ftv.setVisible(true);
		/*
		// =============================================
		// ������� �.
		// ������� 1. � ������ ������ ����� � ������� �������� ��������� 
		potok.resultStr = "������� �.\n������� 1. � ������ ������ ����� � ������� �������� ���������\n";
		potok.resultStr += " 1.1.- ������ ������ ��������� BufferedReader\n";
		potok.resultStr += "    ��������� ����� in_BufferedReader(String path, String name):\n";
		potok.tv.textarea.append(potok.resultStr);
		BufferedReader bufr = potok.in_BufferedReader("e://eclipse_temp//", "result5.txt");
		potok.resultStr = " 1.2. ���������� ������� - ����� readTxt(BufferedReader b):\n";
		potok.resultStr += "    � ����� delStr(String s):\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.readTxt(bufr);
		potok.resultStr = "    ��������� ��������� - Hello. I'm Alexander!\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.delStr("Hello. I'm Alexander!");
		potok.resultStr = " 1.3. ������ ���������� � ����:\n";
		potok.resultStr += " 1.3.1. ������� - out_FileWriter(String path, String name, boolean t):\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_FileWriter("e://eclipse_temp//", "temp4.txt", true);
		potok.tv.Directory = "e://eclipse_temp//";
		potok.tv.Filename = "result5.txt";
		potok.tv.Vertion = 0;
		potok.resultStr = " 1.3.2. ������� - out_CharArrayWriter(String path, String name):\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_CharArrayWriter("e://eclipse_temp//", "temp5.txt");
		potok.resultStr += " 1.3.3. ������� - out_PrintWriter(String path, String name):\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_PrintWriter("e://eclipse_temp//", "temp6.txt");
		// ===========
		// ������� 2. � ������ ������ ������������� �.����� �����
		// � �������� ��������� �� ��������� ���� �����
		// ������ ���� �������� ���������
		potok.resultStr = "\n������� 2. � ������ ������ ������������� �.����� �����\n";
		potok.resultStr += "� �������� ��������� �� ��������� ���� �����\n������ ���� �������� ���������\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.resultStr = "	2.1. - ������ ���� ����� BufferedReader()\n";
		potok.tv.textarea.append(potok.resultStr);
		bufr = potok.in_BufferedReader("e://eclipse_temp//", "result6.txt");
		potok.readTxt(bufr);
		potok.resultStr = "	2.2. - ��������� - I'm Alexander! ������ �� ��������� - I'm Alexander S. Kruglikov!\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.change_str("I'm Alexander!", "I'm Alexander S. Kruglikov!");
		potok.resultStr = " 2.3. ������ ���������� � ����:\n";
		potok.resultStr += " 2.3.1. ������� - out_PrintWriter():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_PrintWriter("e://eclipse_temp//", "temp7.txt");
		potok.resultStr = " 2.3.2. ������� - out_FileWriter():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_FileWriter("e://eclipse_temp//", "temp8.txt", true);
		potok.resultStr = " 2.3.3. ������� - out_CharArrayWriter():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_CharArrayWriter("e://eclipse_temp//", "temp9.txt");
		potok.resultStr = " 2.3.4. ������� - out_BufferedWriter():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_BufferedWriter("e://eclipse_temp//", "temp10.txt", false);
		potok.resultStr = " 2.3.5. ������� - out_UniversalWriter():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_UniversalWriter("e://eclipse_temp//", "temp11.txt", true);
			// ������ ����� ����� FileReader()->Scanner()
		potok.resultStr = "	2.1. - ������ ����� ����� FileReader()->Scanner()\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.in_Scanner("e://eclipse_temp//", "result7.txt");
		potok.resultStr = "	2.2. - ������ - '������� � ������.' �� - '� ��� ��� ������� � ������!'\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.change_str("������� � ������.", "� ��� ��� ������� � ������!");
		potok.resultStr = " 2.3. ������ ���������� � ���� ������� - out_UniversalWriter():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.out_UniversalWriter("e://eclipse_temp//", "temp12.txt", false);
			// ������ ����� ����� RandomAccessFile()
		potok.resultStr = "	2.1. - ������ ����� ����� ������ ����� ����� RandomAccessFile()\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.inOut_RandomAccessFile("e://eclipse_temp//", "result13.txt", "r");
		potok.resultStr = "	2.2. - ������ - 'Good luck!' �� - 'Good by!'\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.change_str("Good luck!","Good by!");
		potok.resultStr = " 2.3. ������ ���������� � ���� ������� - inOut_RandomAccessFile():\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.inOut_RandomAccessFile("e://eclipse_temp//", "temp14.txt", "rw");
		// ===========
		// ������� 3. � ������ ������ ����� �����, ������������ � ������� �����
		potok.resultStr = "\n������� 3. � ������ ������ ����� �����, ������������ � ������� �����\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.glasnLitters("e://eclipse_temp//", "result4.txt");
		potok.out_PrintWriter("e://eclipse_temp//", "temp15.txt");
		potok.out_FileWriter("e://eclipse_temp//", "temp16.txt", false);
		potok.out_CharArrayWriter("e://eclipse_temp//", "temp17.txt");
		potok.out_BufferedWriter("e://eclipse_temp//", "temp18.txt", false);
		potok.out_UniversalWriter("e://eclipse_temp//", "temp19.txt", false);
		// ===========
		// ������� 4. ����� � ������� ����� ������ � ������� ��������� �����
		// ������ ����� ��������� � ������ ������ ���������� �����
		potok.resultStr = "\n������� 4. ����� � ������� ����� ������ � ������� ��������� �����\n������ ����� ��������� � ������ ������ ���������� �����\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.in_Scanner("e://eclipse_temp//", "result4.txt");
		potok.endStartLitters();
		potok.out_CharArrayWriter("e://eclipse_temp//", "temp20.txt");
		// ===========
		// ������� 5. ����� � ������ ���������� ����� ���� ������ ������
		potok.resultStr = "\n������� 5. ����� � ������ ���������� ����� ���� ������ ������\n";
		potok.tv.textarea.append(potok.resultStr);
		bufr = potok.in_BufferedReader("e://eclipse_temp//", "temp21.txt");
		potok.readTxt(bufr);
		potok.findNumber();
		potok.out_UniversalWriter("e://eclipse_temp//", "temp22.txt", false);
		// ===========
		// ������� 6. ����� ������� ������������� ���� �� ������ � ������ ������ ������
		potok.resultStr = "\n������� 6. ����� ������� ������������� ���� �� ������ � ������ ������ ������\n";
		potok.tv.textarea.append(potok.resultStr);		
		String spisok[] = {"���","����","����","����","����"};
		potok.countWords("e://eclipse_temp//", "temp23.txt", spisok);
		potok.out_UniversalWriter("e://eclipse_temp//", "temp24.txt", false);
		// ===========
		// ������� 7. � ������ ����� ������ �������� ������ ����� ����� �� ���������
		potok.resultStr = "\n������� 7. � ������ ����� ������ �������� ������ ����� ����� �� ���������\n";
		potok.tv.textarea.append(potok.resultStr);
		bufr = potok.in_BufferedReader("e://eclipse_temp//", "temp25.txt");
		potok.readTxt(bufr);
		potok.changeLetters();
		potok.countPovtorLittersWords();
		potok.out_UniversalWriter("e://eclipse_temp//", "temp26.txt", false);
		// ===========
		// ������� 8. ���������� ������� ������������� ���� � ���� � ������
		potok.resultStr = "\n������� 8. ���������� ������� ������������� ���� � ���� � ������\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.ChannelRead("e://eclipse_temp//", "result5.txt");
		potok.countPovtorLittersWords();
		potok.out_UniversalWriter("e://eclipse_temp//", "temp27.txt", false);
		// ������� � ��������� java.nio
		// ============================================
		// ������� �.
		// ������� 1. ������� � ��������� ���� ���������� ������ �������.
		// ������������� ���������� ����� �� �����������
		potok.resultStr = "\n������� �.\n������� 1. ������� � ��������� ���� ���������� ������ �������.\n������������� ���������� ����� �� �����������\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.randomIntToFile("e://eclipse_temp//","temp30.txt",10);
		potok.sortRandomIntFile("e://eclipse_temp//","temp30.txt",0);
		// ===========
		// ������� 2. ��������� ����� Java-��������� � ��� ����� public
		// �������� �� ����� private
			// args[6] = 0 - java.io ��������� PrintWriter
			// args[6] = 1 - java.nio ����� ������ �����/������
			// args[6] = 0 - java.nio ����� ������������� ����� � �������
		potok.resultStr = "\n������� 2. ��������� ����� Java-��������� � ��� ����� public\n�������� �� ����� private\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.replaseSubStr("e://eclipse_temp//", "InputOutput4.java", "e://eclipse_temp//", "InputOutput5.java", "public", "private", 1);
		// ===========
		// ������� 3. ��������� ����� Java-��������� � �������� � ������
		// ���� � �������� ������� ������� ������ ������
		potok.resultStr = "\n������� 3. ��������� ����� Java-��������� � �������� � ������\n";
		potok.resultStr += "���� � �������� ������� ������� ������ ������\n";
			// ��������� BufferedReader->FileReader
		potok.resultStr += " 1. ��������� BufferedReader->FileReader\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.reversSymb("e://eclipse_temp//", "InputOutput2.java", "e://eclipse_temp//", "temp28.txt");
			// ��������� java.nio->�����
		potok.resultStr = "\n 2. ��������� java.nio->�����\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.reversSymbNio("e://eclipse_temp//", "temp28.txt", "e://eclipse_temp//", "temp29.txt");
			// ��������� java.nio->������������� ����� � �������
		potok.resultStr = "\n 3. ��������� java.nio->������������� ����� � �������\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.reversSymbNio2("e://eclipse_temp//", "temp29.txt", "e://eclipse_temp//", "temp31.txt");
		// ===========
		// ������� 4. ��������� ����� Java-��������� � � ������ �����
		// ������� 2-� �������� ��� �������� ������� �������� ����������
			// args[4] = 0 - java.io ��������� PrintWriter
			// args[4] = 1 - java.nio ����� ������ �����/������
			// args[4] = 0 - java.nio ����� ������������� ����� � �������
		potok.resultStr = "\n������� 4. ��������� ����� Java-��������� � � ������ �����\n";
		potok.resultStr += "������� 2-� �������� ��� �������� ������� �������� ����������\n";
		potok.tv.textarea.append(potok.resultStr);
		potok.charToUpperCase("e://eclipse_temp/", "InputOutput4.java", "e://eclipse_temp//", "InputOutput6.java", 0);
		potok.charToUpperCase("e://eclipse_temp/", "InputOutput4.java", "e://eclipse_temp//", "InputOutput7.java", 1);
		potok.charToUpperCase("e://eclipse_temp/", "InputOutput4.java", "e://eclipse_temp//", "InputOutput8.java", 2);
		// ===========
		// ������� 5. � �����, ���������� ������� ��������� � �� ������, �������� ���������� 
		// ������� ������� ��� ���������, ������� ����� ������� ��� ����� "7"
		potok.student("e://eclipse_temp/", "student.txt", 6);	//RandomAccessFile + Java.nio (MappedByteBuffer)
		potok.student2("e://eclipse_temp/", "student2.txt", 8); //Java.nio - ������������� � ������� 
		// ===========
		// ������� 6. ���� �������� �������, �����, ����� ����� � ����� � ��������� �������.
		// ���������� ��� ������, ��� ������� �������� �� ��������� ������
		potok.unalisLexem("e://eclipse_temp/", "analisLex_in.txt", "e://eclipse_temp/", "analisLex_res.txt","double"," \\s*");
		*/
		// ������� 7. �� ����� ������� ��� �����, ���������� �� 3 �� 5 ��������, �� ��� ����
		// �� ������ ������ ������ ���� ������� ������ ������������ ������ ���-�� ����� ����
		potok.deleteWords("e://eclipse_temp/", "deleteWords.txt", 3, 5);
		// ������� 8. ��������� ����� Java-��������� � ������� �� ���� ��� "������" ������� �
		// ���������, ������� ������ ����������� ��� ���������� ����������
		potok.deleteProbelTab("e://eclipse_temp/", "InputOutput9.java");
	}
}
