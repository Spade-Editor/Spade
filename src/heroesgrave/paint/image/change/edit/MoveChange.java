// {INSERT_LICENSE}

package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IMaskChange;
import heroesgrave.paint.io.Serialised;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MoveChange implements IMaskChange, Serialised
{
	public short dx, dy;
	
	public MoveChange(short dx, short dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public boolean moved(short dx, short dy)
	{
		if(this.dx == dx && this.dy == dy)
			return false;
		this.dx = dx;
		this.dy = dy;
		return true;
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.move(dx, dy);
	}
	
	@Override
	public MoveChange encode()
	{
		return this;
	}
	
	@Override
	public MoveChange decode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeShort(dx);
		out.writeShort(dy);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		dx = in.readShort();
		dy = in.readShort();
	}
	
	@Override
	public boolean isMarker()
	{
		return false;
	}
}
