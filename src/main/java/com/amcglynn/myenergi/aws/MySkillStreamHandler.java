package com.amcglynn.myenergi.aws;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MySkillStreamHandler extends SkillStreamHandler {
    public MySkillStreamHandler() {
        super(Skills.standard()
                .addRequestHandler(new WelcomeRequestHandler())
                .addRequestHandler(new SetChargeModeIntentHandler())
                .build());
    }
}
