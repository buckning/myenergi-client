package com.amcglynn.myenergi.aws;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amcglynn.myenergi.aws.intentHandlers.ChargeMyCarIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.FallbackIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.GetEnergyUsageIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.GoGreenIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.LaunchHandler;
import com.amcglynn.myenergi.aws.intentHandlers.SetChargeModeIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.StartBoostIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.StopBoostIntentHandler;
import com.amcglynn.myenergi.aws.intentHandlers.ZappiSummaryIntentHandler;
import com.amcglynn.myenergi.service.ZappiService;

public class MySkillStreamHandler extends SkillStreamHandler {

    public MySkillStreamHandler() {
        this(new ZappiService());
    }

    public MySkillStreamHandler(ZappiService zappiService) {
        super(Skills.standard()
                .addRequestHandler(new LaunchHandler())
                .addRequestHandler(new FallbackIntentHandler())
                .addRequestHandler(new StartBoostIntentHandler(zappiService))
                .addRequestHandler(new ZappiSummaryIntentHandler(zappiService))
                .addRequestHandler(new StopBoostIntentHandler(zappiService))
                .addRequestHandler(new SetChargeModeIntentHandler(zappiService))
                .addRequestHandler(new ChargeMyCarIntentHandler(zappiService))
                .addRequestHandler(new GoGreenIntentHandler(zappiService))
                .addRequestHandler(new GetEnergyUsageIntentHandler(zappiService))
                .build());
    }
}
