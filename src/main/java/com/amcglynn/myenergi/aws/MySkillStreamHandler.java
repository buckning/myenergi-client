package com.amcglynn.myenergi.aws;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amcglynn.myenergi.aws.intentHandlers.ChargeMyCarIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.GoGreenIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.LaunchHandler;
import com.amcglynn.myenergi.aws.intentHandlers.SetChargeModeIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.StopBoostIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.ZappiSummaryIntentHandler;

public class MySkillStreamHandler extends SkillStreamHandler {
    public MySkillStreamHandler() {
        super(Skills.standard()
                .addRequestHandler(new LaunchHandler())
                .addRequestHandler(new ZappiSummaryIntentHandler())
                .addRequestHandler(new StopBoostIntentHandler())
                .addRequestHandler(new SetChargeModeIntentHandler())
                .addRequestHandler(new ChargeMyCarIntentHandler())
                .addRequestHandler(new GoGreenIntentHandler())
                .build());
    }
}
