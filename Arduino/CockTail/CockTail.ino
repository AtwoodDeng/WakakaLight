int redPin1 = 2;
int greenPin1 = 3;
int bluePin1 = 4;

int redPin2 = 5;
int greenPin2 = 6;
int bluePin2 = 7;

int redPin3 = 8;
int greenPin3 = 9;
int bluePin3 = 10;

int isPouredPin = 11;
int isPouringPin = 12;

const int init_frequency = 9600;

byte id = 1;
byte data[4];
byte isPoured, isPouring, isShaking;
byte preIsPouring, shakingTimer, shakingTimes, startCounting;

int timer;
int stopShaking;

void setup()
{
  Serial.begin(init_frequency);
  setColor(0, 0, 0, 1);
  setColor(0, 0, 0, 2);
  setColor(0, 0, 0, 3);
  data[0] = id;
  data[3] = '#';
  startCounting = 0;
  timer = 0;
  
  pinMode(isPouredPin , INPUT);
  pinMode(isPouringPin , INPUT);
  
  pinMode(redPin1, OUTPUT);
  pinMode(greenPin1, OUTPUT);
  pinMode(bluePin1, OUTPUT);
  
  pinMode(redPin2, OUTPUT);
  pinMode(greenPin2, OUTPUT);
  pinMode(bluePin2, OUTPUT);
  
  pinMode(redPin3, OUTPUT);
  pinMode(greenPin3, OUTPUT);
  pinMode(bluePin3, OUTPUT);
}

void loop(){
  /*
  if(stopShaking) {
    if(timer < 100) {
      timer++;
    } else {
      stopShaking = 0;
    }
  } else {
    testShaking();
  }
  */
  if(digitalRead(isPouringPin) == LOW) {
    isShaking = 1;
  }
  
  if(Serial.available()) {
    readData();
    printData();
  }
  delay(10);
}

void readData() {
  byte r, g, b, temp;
  if(Serial.available() >= 10) {
    for(int i = 1; i <= 3; i++) {
      r = Serial.read();
      g = Serial.read();
      b = Serial.read();
      setColor(r, g, b, i);
    }
    temp = Serial.read();
    if(temp == '*') {
      isShaking = 0;
      timer = 0;
      stopShaking = 1;
    }
  }
}

void testShaking() {
  if(digitalRead(isPouringPin) == HIGH) {
    isPouring = 1;
  } else {
    isPouring = 0;
  }
  
  if(startCounting == 0 && isPouring == 1) {
    preIsPouring = 1;
    shakingTimer = 0;
    shakingTimes = 0;
    startCounting = 1;
  }
  if(startCounting) {
    if(shakingTimer == 100) {
      startCounting = 0;
      if(shakingTimes > 1) {
        isShaking = 1;
      }
    } else {
      if(isPouring != preIsPouring) {
        shakingTimes++;
        preIsPouring = isPouring;
      }
      shakingTimer++;
    }
  }
}

void printData() {
  //if(digitalRead(isPouredPin) == HIGH) {
  if(digitalRead(isPouredPin) == LOW) {
    isPoured = 1;
  } else {
    isPoured = 0;
  }
  
  data[1] = isPoured;
  data[2] = isShaking;
  
  Serial.write(data, 4);
}

void setColor(int red, int green, int blue, int id)
{
  analogWrite(id * 3 - 1, 255-red);
  analogWrite(id * 3, 255-green);
  analogWrite(id * 3 + 1, 255-blue);  
}
