package com.amcglynn.myenergi.aws;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amcglynn.myenergi.aws.intentHandlers.SetChargeModeIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.ZappiSummaryIntentHandler;

public class MySkillStreamHandler extends SkillStreamHandler {
    public MySkillStreamHandler() {
        super(Skills.standard()
                .addRequestHandler(new ZappiSummaryIntentHandler())
                .addRequestHandler(new SetChargeModeIntentHandler())
                .build());
    }
}
