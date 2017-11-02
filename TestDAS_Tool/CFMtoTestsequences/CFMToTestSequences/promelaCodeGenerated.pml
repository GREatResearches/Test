bool isBtFull = false, isBtNormal = false, isBtLow = false, hasPwSource = false;
bool Text = true, Video = false, Image = true, Documents = true, A = false, B = false, C = false;
mtype = {ImageOn,ImageOff,VideoOn,VideoOff,contextChanged,done,adapted};
int numAnswers = 0;

chan buss = [0] of {mtype};

active proctype ImageActuator() {
do
:: atomic{ buss?ImageOn -> Image = true }
:: atomic{ buss?ImageOff -> Image = false }
od
}

active proctype VideoActuator() {
do
:: atomic{ buss?VideoOn -> Video = true }
:: atomic{ buss?VideoOff -> Video = false }
od
}

active proctype ContextManager() {
isBtFull= true;buss!contextChanged;buss?adapted
do
od
}

active proctype Controller() {
 do
  :: buss?contextChanged -> run AR1();run AR2();run AR3();run AR4();run AR5();numAnswers = 0;
  do
	 :: (numAnswers != 5) -> buss?done; numAnswers = numAnswers + 1;
	 :: else -> break
  od
  buss!adapted
 od
}
proctype AR1() {
if
 :: (isBtLow == true ) -> buss!ImageOff;buss!VideoOff;buss!done
 :: else -> buss!done
fi
}


proctype AR2() {
if
 :: (isBtLow == true && hasPwSource == true ) -> buss!ImageOn;buss!VideoOff;buss!done
 :: else -> buss!done
fi
}


proctype AR3() {
if
 :: (isBtNormal == true ) -> buss!ImageOn;buss!VideoOff;buss!done
 :: else -> buss!done
fi
}


proctype AR4() {
if
 :: (isBtNormal == true && hasPwSource == true ) -> buss!ImageOn;buss!VideoOn;buss!done
 :: else -> buss!done
fi
}


proctype AR5() {
if
 :: (isBtFull == true ) -> buss!ImageOn;buss!VideoOn;buss!done
 :: else -> buss!done
fi
}

ltl pro1 {[] ((Text -> Documents) && (Video -> Documents) && (Image -> Documents) && (A -> Documents) && (B -> Documents) && (C -> Documents) && (Documents -> (Video || Image )) && (A -> (!C)) && (A -> (B)))}
ltl pro21 {<> (isBtLow == true )}
ltl pro22 {<> (isBtLow == true && hasPwSource == true )}
ltl pro23 {<> (isBtNormal == true )}
ltl pro24 {<> (isBtNormal == true && hasPwSource == true )}
ltl pro25 {<> (isBtFull == true )}
ltl pro31 {[] ((isBtLow == true ) -> <> (Image == false && Video == false))}
ltl pro32 {[] ((isBtLow == true && hasPwSource == true ) -> <> (Image == true && Video == false))}
ltl pro33 {[] ((isBtNormal == true ) -> <> (Image == true && Video == false))}
ltl pro34 {[] ((isBtNormal == true && hasPwSource == true ) -> <> (Image == true && Video == true))}
ltl pro35 {[] ((isBtFull == true ) -> <> (Image == true && Video == true))}
ltl pro41 {<> Image}
ltl pro42 {<> Video}
ltl pro51 {<> !Image}
ltl pro52 {<> !Video}

