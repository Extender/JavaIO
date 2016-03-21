public class IO
{
	public static int BufferExtensionHeap=128; // When a buffer needs to be extended, it will be extended to the required amount of bits + "BufferExtensionHeap".
	
	public static byte peekInt8(byte[] buffer,int pos)
	{
		return buffer[pos];
	}
	
	public static short peekInt16(byte[] buffer,int pos)
	{
		short i=(short)(buffer[pos]&0xFF);
		i|=((short)(buffer[pos+1]&0xFF))<<8;
		return i;
	}
	
	public static int peekInt32(byte[] buffer,int pos)
	{
		int i=(int)(buffer[pos]&0xFF);
		i|=((int)(buffer[pos+1]&0xFF))<<8;
		i|=((int)(buffer[pos+2]&0xFF))<<16;
		i|=((int)(buffer[pos+3]&0xFF))<<24;
		return i;
	}
	
	public static long peekInt64(byte[] buffer,int pos)
	{
		long i=(long)(buffer[pos]&0xFF);
		i|=((long)(buffer[pos+1]&0xFF))<<8;
		i|=((long)(buffer[pos+2]&0xFF))<<16;
		i|=((long)(buffer[pos+3]&0xFF))<<24;
		i|=((long)(buffer[pos+4]&0xFF))<<32;
		i|=((long)(buffer[pos+5]&0xFF))<<40;
		i|=((long)(buffer[pos+6]&0xFF))<<48;
		i|=((long)(buffer[pos+7]&0xFF))<<56;
		return i;
	}
	
	public static double peekDouble(byte[] buffer,int pos)
	{
		long i=peekInt64(buffer,pos);
		long f=peekInt64(buffer,pos+8);
		return ((double)i)+((double)((double)f)/((double)(Math.pow(2,52))));
	}

	public static byte[] peekFixedLengthData(byte[] buffer,int pos)
	{
		int length=peekInt32(buffer,pos);
		byte[] out=new byte[length];
		System.arraycopy(buffer,pos+4,out,0,length);
		return out;
	}

	public static byte[] peekZeroTerminatedData(byte[] buffer,int pos)
	{
		int length=0;
		for(int i=0;;i++)
		{
			if(buffer[pos+i]==0)
				break;
			length++;
		}
		byte[] out=new byte[length];
		System.arraycopy(buffer,pos,out,0,length);
		return out;
	}
	
	public static byte[] peekRawData(byte[] buffer,int pos,int length)
	{
		byte[] out=new byte[length];
		System.arraycopy(buffer,pos,out,0,length);
		return out;
	}

	public static byte posBasedReadInt8(byte[] buffer,RefInt pos)
	{
		return buffer[pos.value++];
	}
	
	public static short posBasedReadInt16(byte[] buffer,RefInt pos)
	{
		short i=(short)(buffer[pos.value++]&0xFF);
		i|=((short)(buffer[pos.value++]&0xFF))<<8;
		return i;
	}
	
	public static int posBasedReadInt32(byte[] buffer,RefInt pos)
	{
		int i=(int)(buffer[pos.value++]&0xFF);
		i|=((int)(buffer[pos.value++]&0xFF))<<8;
		i|=((int)(buffer[pos.value++]&0xFF))<<16;
		i|=((int)(buffer[pos.value++]&0xFF))<<24;
		return i;
	}
	
	public static long posBasedReadInt64(byte[] buffer,RefInt pos)
	{
		long i=(long)(buffer[pos.value++]&0xFF);
		i|=((long)(buffer[pos.value++]&0xFF))<<8;
		i|=((long)(buffer[pos.value++]&0xFF))<<16;
		i|=((long)(buffer[pos.value++]&0xFF))<<24;
		i|=((long)(buffer[pos.value++]&0xFF))<<32;
		i|=((long)(buffer[pos.value++]&0xFF))<<40;
		i|=((long)(buffer[pos.value++]&0xFF))<<48;
		i|=((long)(buffer[pos.value++]&0xFF))<<56;
		return i;
	}
	
	public static double posBasedReadDouble(byte[] buffer,RefInt pos)
	{
		long i=posBasedReadInt64(buffer,pos);
		long f=posBasedReadInt64(buffer,pos);
		return ((double)i)+((double)((double)f)/((double)(Math.pow(2,52))));
	}

	public static byte[] posBasedReadFixedLengthData(byte[] buffer,RefInt pos)
	{
		int length=posBasedReadInt32(buffer,pos);
		byte[] out=new byte[length];
		System.arraycopy(buffer,pos.value/*Incremented by 4 above*/,out,0,length);
		pos.value+=length;
		return out;
	}

	public static byte[] posBasedReadZeroTerminatedData(byte[] buffer,RefInt pos)
	{
		int length=0;
		for(int i=0;;i++)
		{
			if(buffer[pos.value+i]==0)
				break;
			length++;
		}
		byte[] out=new byte[length];
		System.arraycopy(buffer,pos.value,out,0,length);
		pos.value+=length+1;
		return out;
	}
	
	public static byte[] posBasedReadRawData(byte[] buffer,RefInt pos,int length)
	{
		byte[] out=new byte[length];
		System.arraycopy(buffer,pos.value,out,0,length);
		pos.value+=length;
		return out;
	}
	
	public static void writeInt8(byte[] buffer,byte i,RefInt pos)
	{
		buffer[pos.value++]=i;
	}
	
	public static void writeInt16(byte[] buffer,short i,RefInt pos)
	{
		buffer[pos.value++]=(byte)i;
		buffer[pos.value++]=(byte)(i>>>8);
	}
	
	public static void writeInt32(byte[] buffer,int i,RefInt pos)
	{
		buffer[pos.value++]=(byte)i;
		buffer[pos.value++]=(byte)(i>>>8);
		buffer[pos.value++]=(byte)(i>>>16);
		buffer[pos.value++]=(byte)(i>>>24);
	}
	
	public static void writeInt64(byte[] buffer,long i,RefInt pos)
	{
		buffer[pos.value++]=(byte)i;
		buffer[pos.value++]=(byte)(i>>>8);
		buffer[pos.value++]=(byte)(i>>>16);
		buffer[pos.value++]=(byte)(i>>>24);
		buffer[pos.value++]=(byte)(i>>>32);
		buffer[pos.value++]=(byte)(i>>>40);
		buffer[pos.value++]=(byte)(i>>>48);
		buffer[pos.value++]=(byte)(i>>>56);
	}
	
	public static void writeDouble(byte[] buffer,double i,RefInt pos)
	{
		long n=(long)i;
		long f=Math.round((i-((double)n))*((double)Math.pow(2, 52)));
		writeInt64(buffer,n,pos);
		writeInt64(buffer,f,pos);
	}
	
	public static void writeFixedLengthData(byte[] buffer,byte[] data,RefInt pos)
	{
		writeInt32(buffer,data.length,pos);
		System.arraycopy(data,0,buffer,pos.value,data.length);
		pos.value+=data.length;
	}
	
	public static void writeZeroTerminatedData(byte[] buffer,byte[] data,RefInt pos)
	{
		System.arraycopy(data,0,buffer,pos.value,data.length);
		buffer[pos.value+data.length]=0;
		pos.value+=data.length+1;
	}
	
	public static void writeRawData(byte[] buffer,byte[] data,RefInt pos)
	{
		System.arraycopy(data,0,buffer,pos.value,data.length);
		pos.value+=data.length;
	}
	
	public static void bufferCheck(RefByteArray buffer,int currentPos,int newPos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		if(bufferSize.value<=newPos)
		{
			bufferSize.value=newPos+BufferExtensionHeap;
			byte[] newBuffer=new byte[bufferSize.value];
			System.arraycopy(buffer.value,0,newBuffer,0,currentPos);
			buffer.value=newBuffer;
		}
	}
	
	public static void writeInt8ToBuffer(RefByteArray buffer,byte i,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+1,bufferSize);
		writeInt8(buffer.value,i,pos);
	}
	
	public static void writeInt16ToBuffer(RefByteArray buffer,short i,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+2,bufferSize);
		writeInt16(buffer.value,i,pos);
	}
	
	public static void writeInt32ToBuffer(RefByteArray buffer,int i,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+4,bufferSize);
		writeInt32(buffer.value,i,pos);
	}
	
	public static void writeInt64ToBuffer(RefByteArray buffer,long i,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+8,bufferSize);
		writeInt64(buffer.value,i,pos);
	}
	
	public static void writeDoubleToBuffer(RefByteArray buffer,double i,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+16,bufferSize);
		writeDouble(buffer.value,i,pos);
	}
	
	public static void writeFixedLengthDataToBuffer(RefByteArray buffer,byte[] data,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+4+data.length,bufferSize);
		writeFixedLengthData(buffer.value,data,pos);
	}
	
	public static void writeZeroTerminatedDataToBuffer(RefByteArray buffer,byte[] data,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+1+data.length,bufferSize);
		writeZeroTerminatedData(buffer.value,data,pos);
	}
	
	public static void writeRawDataToBuffer(RefByteArray buffer,byte[] data,RefInt pos,RefInt bufferSize)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		bufferCheck(buffer,pos.value,pos.value+data.length,bufferSize);
		writeRawData(buffer.value,data,pos);
	}
	
	public static void putInt8(byte[] buffer,byte i,int pos)
	{
		buffer[pos]=i;
	}
	
	public static void putInt16(byte[] buffer,short i,int pos)
	{
		buffer[pos]=(byte)i;
		buffer[pos+1]=(byte)(i>>>8);
	}
	
	public static void putInt32(byte[] buffer,int i,int pos)
	{
		buffer[pos]=(byte)i;
		buffer[pos+1]=(byte)(i>>>8);
		buffer[pos+2]=(byte)(i>>>16);
		buffer[pos+3]=(byte)(i>>>24);
	}
	
	public static void putInt64(byte[] buffer,long i,int pos)
	{
		buffer[pos]=(byte)i;
		buffer[pos+1]=(byte)(i>>>8);
		buffer[pos+2]=(byte)(i>>>16);
		buffer[pos+3]=(byte)(i>>>24);
		buffer[pos+4]=(byte)(i>>>32);
		buffer[pos+5]=(byte)(i>>>40);
		buffer[pos+6]=(byte)(i>>>48);
		buffer[pos+7]=(byte)(i>>>56);
	}
	
	public static void putDouble(byte[] buffer,double i,int pos)
	{
		long n=(long)i;
		long f=Math.round((i-((double)n))*((double)Math.pow(2, 52)));
		putInt64(buffer,n,pos);
		putInt64(buffer,f,pos+8);
	}
	
	public static void putFixedLengthData(byte[] buffer,byte[] data,int pos)
	{
		putInt32(buffer,data.length,pos);
		System.arraycopy(data,0,buffer,pos+4,data.length);
	}
	
	public static void putZeroTerminatedData(byte[] buffer,byte[] data,int pos)
	{
		System.arraycopy(data,0,buffer,pos,data.length);
		buffer[pos+data.length]=0;
	}
	
	public static void putRawData(byte[] buffer,byte[] data,int pos)
	{
		System.arraycopy(data,0,buffer,pos,data.length);
	}
}
