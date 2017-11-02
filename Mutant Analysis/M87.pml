int battery = 3;
bool hasPwSrc = false, image = false, video = false, mobileGuide=true, showDocs=true, text=true;
mtype = {imageOn, imageOff, videoOn, videoOff,contextChanged,done,adapted}
int numAnswer = 0;

chan buss = [0] of {mtype};

active proctype ImageActuator() {
	do
		:: atomic{ buss?imageOn -> image = true }
		:: atomic{ buss?imageOff -> image = false }
	od	
}

active proctype VideoActuator() {
	do
		:: atomic{ buss?videoOn -> video = true }
		:: atomic{ buss?videoOff -> video = false }
	od
}

active proctype Contextmanage() {
	battery = 3; hasPwSrc = false;
	buss!contextChanged;buss?adapted
	do
		:: (battery == 1 && hasPwSrc == false) -> hasPwSrc = true; buss!contextChanged;buss?adapted
		:: (battery == 1 && hasPwSrc == true) -> battery = battery + 1; buss!contextChanged;buss?adapted
		:: (battery == 2 && hasPwSrc == false) -> battery = battery - 1; buss!contextChanged;buss?adapted
		:: (battery == 2 && hasPwSrc == true) -> battery = battery + 1; buss!contextChanged;buss?adapted
		:: (battery == 3 && hasPwSrc == false) -> battery = battery - 1; buss!contextChanged;buss?adapted
		:: (battery == 3 && hasPwSrc == true) -> hasPwSrc = false; buss!contextChanged;buss?adapted;break
	od
}

active proctype Controller() {
	do
		:: buss?contextChanged -> 
			numAnswer = 5;
			do 
				:: (numAnswer != 5) -> buss?done; numAnswer = numAnswer + 1;
				:: else -> break
			od
			buss!adapted	
	od	 
}


ltl pro01 {[] ((showDocs -> mobileGuide) && (text -> showDocs) &&  (image -> showDocs) && (video -> showDocs) && 
	(mobileGuide -> showDocs) && (showDocs -> text))}
ltl pro21 {<> (battery == 1 && hasPwSrc == false)}
ltl pro22 {<> (battery == 1 && hasPwSrc == true)}
ltl pro23 {<> (battery == 2 && hasPwSrc == false)}
ltl pro24 {<> (battery == 2 && hasPwSrc == true)}
ltl pro25 {<> (battery == 3)}
ltl pro31 {[] ((battery == 1 && hasPwSrc == false) -> <>(battery == 1 && hasPwSrc == false && image == false && video == false))}
ltl pro32 {[] ((battery == 1 && hasPwSrc == true) -> <>(battery == 1 && hasPwSrc == true && image == true && video == false))}
ltl pro33 {[] ((battery == 2 && hasPwSrc == false) -> <>(battery == 2 && hasPwSrc == false && image == true && video == false))}
ltl pro34 {[] ((battery == 2 && hasPwSrc == true) -> <>(battery == 2 && hasPwSrc == true && image == true && video == true))}
ltl pro35 {[] ((battery == 3) -> <>(battery == 3 && image == true && video == true))}
ltl pro41 {<> image}
ltl pro42 {<> video}
ltl pro51 {<> !image}
ltl pro52 {<> !video}
