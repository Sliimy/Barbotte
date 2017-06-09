package barbotte;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
public class PinManager {
	private GpioPinDigitalOutput pinGame;
	private GpioPinDigitalInput pinButton;
	private GpioPinDigitalInput pinVictory;
	private GpioPinDigitalInput pinCaptor;
	private GpioPinDigitalInput pinEndGame;
	
	
	
	public PinManager (){
    	GpioController gpio = GpioFactory.getInstance();
    	pinGame=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "PinGame", PinState.LOW);
    	pinButton=gpio.provisionDigitalInputPin(RaspiPin.GPIO_02);    	
    	pinEndGame=gpio.provisionDigitalInputPin(RaspiPin.GPIO_00);
	pinCaptor =gpio.provisionDigitalInputPin(RaspiPin.GPIO_03);
	pinVictory=gpio.provisionDigitalInputPin(RaspiPin.GPIO_21);
	}
	
	public void highPin (int numPin){
		switch (numPin){
		case 1 :
			pinGame.high();
			break;
		}
	}
	
	public void lowPin (int numPin){
		switch (numPin){
		case 1 :
			pinGame.low();
			break;
		}
	}
	
	public Boolean readPin (int numPin){
		Boolean res=false;
		switch (numPin){
		case 2 :
			res=pinButton.isHigh();
			break;
		case 5 :
			res=pinVictory.isHigh();
			break;
		case 4 :
			res=pinCaptor.isHigh();
			break;
		case 3 :
			res=pinEndGame.isHigh();
			break;
		}
		return res;
	}
}

