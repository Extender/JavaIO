public class RefByteArray
{
	public byte[] value;
	
	public RefByteArray(byte[] _value)
	{
		// Note: Use RefByteArray.value instead of the original byte buffer after using the IO buffer write functions!
		// The buffer needs to be trimmed using "IO.trimBuffer" after usage to strip it of the excess allocated bytes!
		value=_value;
	}
}
