rem ���~�Q�[���v���O�����A�T�[�o�[�@�\�̊J�n�o�b�`

rem config.properties�X�V�o�b�`�t�@�C���Ăяo��
call configUpdater.bat

rem ActiveMQ�̃o�b�`�t�@�C��������f�B���N�g���Ɉړ�
pushd .\apache-activemq-5.9.1\bin

rem ActiveMQ�̃o�b�`���s
start activemq.bat

rem ���̃f�B���N�g���ɖ߂�
popd

rem ���~�v���O������jar�t�@�C�������s
java -jar truefalseServer.jar

pause

exit 0