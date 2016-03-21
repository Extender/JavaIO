public class RefByteArray
{
	public byte[] value;
	
	public RefByteArray(byte[] _value)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		value=_value;
	}
}
