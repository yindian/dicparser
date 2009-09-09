/*****************************************************************************
 * 
 * @(#)VoiceDataBase.java  2009/03
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@google.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *****************************************************************************/
package com.jin.dic.ks.voc;

import com.jin.util.BytesUtil;

class VoiceDataBase {

  static String[]         words       = { "xajin", "a", "landlady", "was", "zygote", "bits" };
  static int[]            offsets     = { 1, 11827528, 5801312, 11420992, 288106728, 288107680 };
  static byte[][]         datas       = { null, null, null, null, null, null };
  private static String[] dataStrings = {
      null,
      "2E7261FD000400002E726134000005A80004000000390001000001160000056E0001D3AD0001D3AD00010116000000002B1100000010000104496E743004646E657400030000000000770BA36A5048C02D2C21C32A75FEF97CE7F39FCF7C3EF3F9CFE73E9FF97CE7F39FCF7C3EF3F9CFE73E9FFB7C0FCA07FF408DE0446417297384FF48235FEF33EEBD149EF1A45DA00B58B8503A8052829B63FA8E0DC5BD098EEE8A1A4F8A8036C01F936646B409671430227512EF08CC4CCD69A9CF273146A6D8CAA29DBC2236C1B064805CCF3CBA3C5ED701350015596A40C6F27C8668DB8DD9D52D559085FF0A83EF6AE78B8C06A28053EAF3B93DAE938F416B3A59583C1F026A002BF371F5DF3A5C095677C9EAC598E708DD8FE5D34E4AF5BBC8BB4630FBD007A2987A6A814DAA9E3CCB4ED2D47850048401708AC4B1FF17B96DAEFCBDB4DE1C3264F2567A5F488BF4853E9803B3386001CC94C350AEB09EA0F14CE8770B4C475048F32D2CE1C32B75FEF97CE7F39FCF7C3EF3F9CFE73E9F92F241723F3C77E5E9AF19432401D627439F817E0CFFC2133981D18158F6E6CBE7D7E80D98DD7C6BF03271145A92DF34F4CBB5CA3E46B16154B00CE9979F369D6D4631C3C222CD6062DB63803E4045922D37B568242CD521352A88EC4E55EA79B460A9DA2D6DD40ED6C26AFD33A7AADD2291B5B3007DFF130EAF1E0BA39807858979FE1EC85AF3ADE1AC8641E0936D49414A483518D91D6D72EFB7D2742EA83304201E7F7708CD221D75FE1DACAB37BF957E0BBEDFA3CA35317DB4CDBAB6DBD2DA8432A91815CD466831D470DF0300FC6C30375E60ED203C84E85944A9D41B036A737561B0CF22B6CFA86E12555BBBD609DC98706363009C20E8770BBB135048F22D2CA1C22B5205617FF1F3126BAFC7D39D319BFB74F232405A0920A6EB3B7EB80C3C1FF145F371851E1BB86A67C6ACAFB9C92102E1ECB24AB9D4149FD0AFEBEF45790C01F03EF0CB1A28C278ED0608FDCD917326E5F362CD690F3B699647A56E0A85942E77BB61BD7CE7DA6DCF7DBEF3094AF5426BA67E8A726B53A6C258F934D8BEDFA9A667F673E97D42185A30D7B9570DB72A5515D7B8C401A93C82E26AAF01EB6F0CF6A1CF1408FE115C91DB7C584F4435AB3D8574D69811A01AC310C83801FCE69180B7113621E148049EA663E1F4D69E00D93133BB7EB4ED9139722F0889833DC9C30BC39E720569A96CA8950808790045F6DB7B017BBF4E14352BFC8C54D95C910B636CD6E2DAB580B63A12770BC1875048F22D2CA1C22A3FC0FF48EA629F73A8A99232C38C3A9FAF4D345C6BCF7BA83951F25C043B5095C9CB0877AA93126AF53016EC4DE4DB1BF6A94CACA78A2CF65643495A182A2840A9027714F4F2EB43BE4314B33E732A8B6DBC58B2AD2D5269B6765AA3F0D44F9057ECF9A605EAEED07C3E43D6D8FD6FC0C9F94A8040CB4B3B6E8F51F19048E246F88B6E6A52498AA580ACC037C8D3A21DAD20378C8994C7C8748058818BFDAD35A96A6D3E2D438003F7471385D1B739D0FE4740E0A11F7B1F4FC6281D55A35ACBB5D66BADD654AFB1F998E6F363DBC66C53A90A4FAA12879DAC31421F25AEC22CBE307BAD1E04BDB441A384A592ED3BD5528FA71262A22C0D061BAFDD2670297D341275D18E21682E8938770B73035048F22D2CA1C22B59D04D23B5F51C48E0BEE2ED6AC7CAD2B2F2405E0A6BD0CCCABD07C1526E12C528578163C5B1F1876223D28D3DEB1A6B0FB6498C6D6A1BC29578E7EC1E31C313BF1C4D7D98FB1F4E7EABE9E9EE34E39ED0F20D1498DC11DC5CB0B473AF4D91728CDD6C82D0262B8FF66C3CD96C32CF84D6D2E9B66E2561297D8DF40D2BAFA1016A58E4D2E03CF391A51AA66402B9EAABC47D3294B96148CE2CF88DB90A4EB6AD0EBE462301A4938FDD6DF9D1400F93F6C6697FD8B25DBF2B6B71EB35465CAFBE3598C6B126CBA0DB29DA6AAD7A786B5529A700DF4A6AE9D64ED11B967EEC065263E4C96076C295CB275B7B87FA86EA1B9EF641D7035EE9D73D618827022CABA5A4E28BCEA9679CD9E0EB0052615443000000102E7261340000000852615443000005B8",
      "2E7261FD000400002E726134000006C0000400000039000100000116000006840001D3AD0001D3AD00010116000000002B1100000010000104496E743004646E657400030000000000770BF8C85048F52D2CE1C32BBFFA628798E39F6F7C3EF3F9CFE73E9FD2F2C177243C8D655F777DA6FB6D17E41B275EADEB8D072F5C219895201FC9E27D08C0F907D142C2A75842B22AF0906D0349525D3654084B420895C55847ADB9B36089E8D2CAFD52696DE1A599C2FC344A3752A0FD86505F7D0176FAB090A5D1225FCD813F8CC7D275E703A47D848F13D61AC8B5D6DA6BDB805AFF0A05F931411F8A41797695F0BF6336C3CB207C37D9DEF6B6356ECCB269D64600B519103AEA328CA950672D95FE38CF78EECAE0AAC3A4CD4776828B6DB5A1BE4366780A3938E2F687AF9A5E2857BD6CFA027CED03607E06B9B13FFC6B1EC1D221BEFE565B0A766A65F9E2234629751A5B2B501BA631B22A9B56A50980654C4F770B02B65048F22D2C61C12B3E84F58828F79F6F413DF371EF4763AEF23243567FF0FE1EFD01642B631F01E032DD1C7F5ADABD11A64CD3B5B51C301C0D02B93923C0BB46B6E7D31E4A75E79392CBB0C1C842D5D8D3B5FD54E72BA3CF7CC2CC95D0DC27A2FC1AE86AA13BD63EA54D36824ECB457EE8F3D7EEC172094874D91D5831D6F90BF0B9372C9E10908940242922264A69BE945B0400E212ECF1B7C905FC130052B6255CAFB4B17B9D070E677C5C00B511646F54087D0555776002E347B269DA759585777316087A4D281F40010C478BC4404314B0F8D586C05C3A6B8E6B72CC1E9D10E7536BC2C14E2BD556FB55BB1F0E178A222C5C046301040076073FACA0E7FF0A743DC0600B30B7DD37B16B6BB2E0DA6DB8770B9DB85048F22D2C61C22B0E02BA4E5D0D471651395C0F10D83171F23240650010FC78D5BF1A403D8EA04742A0438936D4786485DB4EA835D8DED451E729B1476D136902C2592BD23A2E4E269D495E97D3F9CF0013C12024B89FF266D81A82C1AE16ECD9BBC84D98EE8EF89584692CBA905838B354DBA6FA80B7BD14B674DCFA8E0FEB52D2129BFD0EE93AAD3EBA3AB4A2D392A944E74B9FD45E524BFDE9E7BF2D4552026F10365C74BBAA33B2D7D6C861086089A09500022D0318576875FB234E245E576A1C0B35D802A89EE502319F2B5461037681711A01D6DEA9B2C65A0DBA016A677A3EE42085CE92D060BDFBD67C35BB6DADA97A23A9805A9665D7E029708F708BB750642A228D345A6B93E5AF9800248E73770BA3CA5048F22D2C61C12BE2C397AE2A1410D671E5634CEF95CABAF232C05DC020DF502D000F6ADAF72B24317A41A20CE4D21C64E8786A7A38A6D82EC02BFDC9E72E614A418CA500608A1FECA51A453DBE54847E9AD5326B80DE053016A0C152766038B364641B71F03B089C5BFF91E807C21DE9A2FBF380322E0C53D5EDE186B17C3DD1011FE80AAEA419105734EF95850D836ACE38783E3A29EC927A77BD1F32B838C592C0EB067C28E34A0C041FBE4DA770B06B1AB1D68EF451559F638C421AD636094B0A03720FACEA6B16F23950582067FD5E37ACB5D44CBE43824FEAB99FFEA8C02769131FEF81FDF785FF4948D0E1F584D27A006F0C6A3100024F6DFFABE5E517FE1064E80E0557B51D9EB1B6FAA04160D4770BED815048F22D2C61C12BBBC35EF5DA33F5C187BFA549D67A6331F232406504305F80DDF1B1BF87564882916BFF1B0181FE1ADC1682703B9CCA36084F6321A80B926AE824B49A0F47DBDC287F41CB57F01FC95A96DADF5C2199871983570C3105E7E30D153668B9C66BA94A98270D6DCDAC46A891F3F9D85ACD325A6B5D80AFD162A3F90977A9C2B77F6407D1707B4D7327C52A27E2A8720E11DBC5E9DB20665C29B1F371A725314F5C55A7AA028472BDDABB4FE97BC4BB0E8F8BBF9F65C623E0E968879DDD8F90E817C15CF68EA3A44540AB855655029BEC4811A000AD8B02BA6FA809A433FBA02E1DC72605ACA99F6AB876B6A640AC1C40B8FEBF1C44120A3DEB179EA3D46F91F3D6DAADB56D7D5A53B4E68E19770B3FAC5048F22D2C61C22A564351B3EAFB9F6E724FF2F9AF97BF1D9256B71A28E8E0BE9AFA306553E1E987C9CB0293DDFE2280BF5C54A62225F2298D2D1B31A06601202302684FBA7EF071028DEA6D5AC014C721A58396AC810968EF0C931B0320FC74BA02EE62E65AB1B5789ADDAC6DADC4A2A1F06B964BBF130EEFF026E0724707CAE0D8AA638859D979E1A687AD5405A3E733ABB34D8BA18984D6204A8CC8A2EA369283B8E949114A2C4E4932D452B0A4F3BF37C7601DFEA8687E5D2BFCEEA8341CF9EC8CA8B681C69C829A5B18D54DBA1E07643A3C924FAD91CBBA468D4BA59B9DA90963A5BE6D536B49C3E0A9EDB93CC7421C42B2E78D1211F2CA439075854408E3D478E8A62D35FBA8EA356A5967001E62AB00000052615443000000102E7261340000000852615443000006D0",
      "2E7261FD000400002E726134000005A80004000000390001000001160000056E0001D3AD0001D3AD00010116000000002B1100000010000104496E743004646E657400030000000000770BF76D5048F62D2CA1C32BD8F54AAFEB1BDC9084DBF3FDCFE73E9FD2B2C46F0002109E641FFB4A57617E7CD753523D5357F31680973E20D7B214EFB3FB01AD1B3A89490FEBBCF944EC4A91B2A2A4A44BB6040A3C575538ABDFFE52E813A16F74C3239650E8AA9C0A7D2795C649AFE7070E1B54E96E19DF7EBF34AB246B149F846EA004B4DE42AE3A899A99B6307D2BB61840AD1E1BFCA6EEEFBBDF027CB4306701097CA926D83E92EDEBA65A5535E5427CED29084FBE067C4D1510728CDAFE5087D79228EBFB0F84781C68211FC31100DA4F98B87C2331D28D0882C2543C053B1C7EA8C3DF4C85D86384F49BC2075F017084FEBC6888882688775904F78AD38BF5285D811B66F54515C7019D464A91F94068D651C0770BF6CE5048F42D2C61C22BEA02DA8538E45F6A8750DD45C834B12CF232C05A8A00D59A37F884492508C09B8F0EA034EAF0C557F0347471299F4DF7F4F315CE753EFA0D1249FC80BD1FF1A5E98B1F73F921D759F7DF6458ED4A48176DAC3E52A48890B0B63924127653D9AA4FF4BA5C6A15D07423A1B41EC929BF5FFD8428CDC96F01F503FE68F1BDFC2C1BC5F98F9B10D309366515C1D0E63960938080211C55C231179D34EB5F35625A03286B5715941A2C4D806E61E907015C257DA74F87BD920FAF045C21437256E3FAD4E7A5C8BB4CA43B57BBF45DFE07FDDFAFCF9BCFCA0CD6F610044943D4EEE1D25CB8B1108A99158EA835BE00C217EC33198A9AA1F590A6E42C3D43DD36696B0DD71ACBD8D26AA05D3AAB770B75AE5048F42D2C61C229C68C2158E8F4E034222755BDE52CDA8A4ADDC60096954C2B14FACD462B5FAA2AB48E50C94AC013BE709B4FA9A9528A529CD485EA959E506ADCD40A572A160CF198BC8490042D47319D1AA9133483C15FAC74D475E6901B42ED4220741C8304F32E01DA66D25BEC4C4104A4F53B8532BABDAEE798C93995B763DB3AC650739006A8FEEA2B31711172C09781A6AD26CD375A6BE3A6BE1890B5394940A4692E9DAE241AE738CF1FE3107AD9B6F135DF6BAD6A5BF96272AA45C83C3544207B9B24E90D6185F310F643A0215E018015002E79F102B1598166380DE04389D1696E9E480B1459AF99D37DCA214D9384BD5C85AE2062E003E04DCA23CB2E0E0282F26BAF8F6B11376AA7D0485002770BA4755048F42D2C61C22BE1874BB92C55A60A86B9D1818F274A5FF2B2C263EC3F7A713CECD0B06EF77C2738DA673AA0B55ED8826A6058D08743DF927E6AB59FEF7E4E34464A2AC468917C57F29D347C42F2F5F0A740FD69DBC7EBA92052763932C1A95CFBA8A66B4DEB1AA535E66B33B55A68D3FA084022B4BA60EFDBE1F7ADA7C11EDC295EC85169D620ACC1387CAD959F63A8650C08EC02EB8DF3FF0D71FE6008C4298DD6BB8DB6B18230052B2EDA5A23410089F3A1806840805C20621FA5D4351F4ACF1F837D53551AAD312C51DAF6226D91A97316B56B65C873A07941633A456803E1EF04C9B0DAC8E8B095AC55AB260B36FA7B456B6ED45CCBD9B26EAD1154428FAAC9BD3471AD04E7539BCFE94AC0F405B3770BF26C5048F42D2C61C32BA909FA84F8B68A6E4AAAB185EDD7DE2DB2F240705BD3CC30F150B033177E983B7A61CE4B6D07592CB5084D8395190FA30F6F5C82B182D51CC78F85BFE13DC9281741A174235523B569BB8CCAB1DADEE04E10BD1B728C35AD9C4E8F3089A79E6C321EACE246B61A5D2CD90E71C0136A1794579381A59A665534ADD4D0A622F39D9F592C42A3A6D6689451A7A68DCE76AA3F8D582635EE6A1A8BD1B8E635D5A336FC87F9EAE7F39FCF7C3EF3F9CFE73E9FF97CE7F39FCF7C3EF3F9CFE73E9FF97CE7F39FCF7C3EF3F9CFE73E9FF97CE7F39FCF7C3EF3F9CFE73E9FF97CA7F2DFB08060082088086502F03F6984EB1DC6FD976233BEB4CB74810B174A07500A70134C5FB161B8D7C0B11EAB0052615443000000102E7261340000000852615443000005B8",
      "2E7261FD000300180001000000140000EA60000003980000000003046C70634A00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000048BA48DE116000000000013E5006F66354488FFC293C2AF429CA7B3EB92D3DD814FE8F7366CF5A988418969D31B3EC7784A32D95CD3E4C40EB477F9CA80F969ED1B5E60B9DAB2799AD88DA4FACABC606A83445B5E18BB821B6B12BC1FD7F809EC6F1AD206C9C38BA9256F8C31C2CA6E80D39191FA954593837326CB8AB327B89BF8D24DC341F93162152A2C04AF5789D9B50BFFB8A855EEE53945CC3E27FCD2E576D5C9DAB304DFAA68DFE499C45A8EF247713D663495D95BAF25706A293FE3B9C7CA754232E7E906326B9753AD1BFEC018CC7801C3FFE58A1BA58AC62A8C8AD4AF0BFCDD2874115940FE8F9A0278BE685A7ABD8AAF03FF339C1CC78EC0FFFBB07C50B789D61BE781AD03FE7DA81181A6C1FF3A9E04C0A7C4105E88D49F19D4DD433D3C6EF0C3B8EA7718C8096844579D23479069B224D04B104EC9E89F50E0A51EF9E5F0AC3F880016024595760C0BC86F0E85252777B5B1B3662C38A91CE4634F6032F677FECA44F147A7F0B53B0DC3D98C8CA151FE0B526B698D85EF53A9A9B0A0D458BA5FDDE9C3313A0A97FBBE263112B9A9AD49DA5234870EA953FF1AE29B1E33266F23A9AAAF57FD8ACAB5C380D629F8EEC7FC8946ED1499E1A961D0C95B66FEA0DDEF66F2E4EDAFE62CA98B93A8A9781A7BFDF4FDDD744A2327F8B1446EB9C992A99393C5FCA79B2163FB3BAF1F812C0127459764975C9BEC04C4D34C41E8F9D6F74C156815665923085941D48B3C89A8BDEF1AF3F4E68A295345872484770A190073D68D9197917064B4164D86D8B76495F398DA51C9E9CAAD23F8177509B00FAAD84327A397B00705FDCF0CF08789ACF4F01EEF279748969EB39CB0257D9B78DFEC266E24BD136E1B7564B714CA0DC2A9BC36CC68E664537609F22D532548FE0933A1AF5CD1F934ABE360FEF47C780C9314593D83020FAEBCD481BA6B837D8A9C4D1AAD11259AE481896061B7000B35FE00969A40010D8B4D68972B01C40398C00007528000D01000065ACACF79B92C800001489000088E80003DFC0003436",
      "2E7261FD000300180001000000140000EA600000030C0000000003046C70634A00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000095AD0ABB9980000000000000000013651446F7804DA9CDB8B325E05F793DC3DCC789B658FAB18B7C6569ADBC3B29F87D474FFF76362FF05BD4E67AC85DAC9FB81B59BFCC7654EB4D769FF3F3767F966059AC9EBCAB2BA001D7E3FD7DB31764E4BCFF847A55CB9EBC3B2DD477CB77FE1803C4FC9220F561EE41CC6EB80A304CF20091DF14E45EFE7B0CCB25AE454B5CB228F51C3C57A584EBA2287BF2D4F46BF49571257AC849A2DA8B21164C236E8DFE1349F93698F838BA483C6370D047C3AD3C897C66D1C5F03EC55758724839271B1CD124C2E505AFE45446C6B6F151977248DBE4BFBF3DDAEFB628C7EA334D7478F5EF663A512BFB1E1915C5C4539C17E4ED744740FA707C524968F82809D9CC8AA27E38866AF2B084F5B5A851398139657543C6C4218E370951473372F555A65AB9A4A7573F25D828323EA8BE634CDB76F5B2AC3AC9B6A7F0053DCFA7E97E779E7F74E248F934AA5AC9B9FB8B28B73C490A6E49EBD7F17A1EFDD0AB5671B1A5E02FFFD900B25E98F582734F02F510C9B6E982FB8F0ACFC88E868E6AC2E974BF16F16BD85B2987B9B12BE9CF2B7929D770A4F13ECAE52EE656D97E559D18EDC52F392E222DA5727BBCD4D4D571C89162C594B526550DBE6AA69071649489D5254E502197414C1D3E452B3E8EAC13F46BF2B91A814DC832292693C4BE65D378D62B3949B0809CF4354AD0291537E456B6877475EC00B6B03944B06F567250135C7E985000086B600C2C660071ACAD76364DB81A801DF90000F93800018E80007576BD1334B63800001DB20000FBF80001D200006A8CA9542651A8000033E1000029880000028000532E" };

  static{
    for(int i = 0; i < dataStrings.length; i++){
      if(dataStrings[i] != null) datas[i] = BytesUtil.revert(dataStrings[i]);
      else datas[i]  = new byte[0];
    }
  }

}
