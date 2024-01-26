clc, clearvars

data = load("DREAMER.mat");

Fs = 256;

df = data.DREAMER.Data;

len = length(df{1}.ECG.stimuli);

len_df = length(df);


for d=1:len_df
    for c=1:len
        len_table = length(df{d}.ECG.stimuli{c}(:,1));
        [hr, locs, hbpermin] = ecg2hb(df{d}.ECG.stimuli{c}(:,1), Fs);
        df{d}.ECG.result{c}(:,1)=hr;
        df{d}.ECG.result{c}(:,2)=locs;
        df{d}.ECG.result{c}(:,3)=hbpermin;
        df{d}.ECG.result{c}(:,4) = df{d}.ScoreValence(c);
        df{d}.ECG.result{c}(:,5) = df{d}.ScoreArousal(c);
        df{d}.ECG.result{c}(:,6) = df{d}.ScoreDominance(c);
        
        df{d}.ECG.result{c}(len_table+1, :) = -100;
        
        fileName = ['data' 'ecg2hr' '.csv'];
        writematrix( df{d}.ECG.result{c}, fileName,'WriteMode' ,'append');
    end
end


function [hr, locs, hbpermin] =  ecg2hb(ecg, Fs)
    ecgsig = (ecg)./200;
    t=1:length(ecgsig);
    tx = t./Fs;

    wt = modwt(ecgsig, 4, 'sym4');
    wtrec = zeros(size(wt));

    wtrec(3:4,:)=wt(3:4,:);

    y= imodwt(wtrec, 'sym4');
    y=abs(y).^2

    avg = mean(y);

    [Rpeaks, locs] = findpeaks(y, t, 'MinPeakHeight',8*avg, 'MinPeakDistance',50);
    hr = [];
    for i=1:length(locs)-1
        hr(end+1) = 60/(locs(i+1)- locs(i));
    end
    
    nohb = length(locs);
    timelimit = length(ecgsig)/Fs;
    hbpermin = (nohb*60)/timelimit;
    locs = locs(2:end)
    
end

