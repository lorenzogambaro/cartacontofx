export const generateIban = (location, abi, cab, cc) =>
{
    let ccn = cc.padStart(12, '0');
    
    let bbanSb = calculateCIN(abi, cab, ccn) + abi + cab + ccn;
                
    let iban = location + calculateCheckDigits(location, bbanSb) + bbanSb;

    return iban;
}

const calculateCIN = (abi, cab, ccn) =>
{
    let bban = abi + cab + ccn;
        
    let aa = "A0B1C2D3E4F5G6H7I8J9K#L#M#N#O#P#Q#R#S#T#U#V#W#X#Y#Z#-#.# #";
    let bb = "B1A0K#P#L#C2Q#D3R#E4V#O#S#F5T#G6U#H7M#I8N#J9W#Z#Y#X# #-#.#";
        
    let sum = 0;
        
    for (let i = 0; i < bban.length; i += 2) 
    {
        sum += Math.floor(aa.indexOf(bban.charAt(i + 1)) / 2);
        sum += Math.floor(bb.indexOf(bban.charAt(i)) / 2);
    }
    sum -= Math.floor(sum / 26) * 26;
        
    return aa.charAt(sum * 2);
}

const calculateCheckDigits = (countryCode, bban) =>
{
    let fakeIban = countryCode + "00" + bban;

    fakeIban = fakeIban.toUpperCase();
    fakeIban = fakeIban.substring(4) + fakeIban.substring(0, 4);
        
    let sb = "";
        
    for (let i = 0; i < fakeIban.length; i++) 
    {
        let c = fakeIban.charAt(i);

        if (isNaN(parseInt(c))) 
            sb += c.charCodeAt(0) - 55;
         else 
            sb += c;
    }

    let remainder = BigInt(sb) % BigInt(97);
    let checkDigits = 98 - Number(remainder);

    return checkDigits.toString().padStart(2, '0');
}